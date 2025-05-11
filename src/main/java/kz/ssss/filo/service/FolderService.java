package kz.ssss.filo.service;

import io.minio.messages.Item;
import kz.ssss.filo.dto.response.FolderInfoResponse;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.exception.minio.DuplicateResourceException;
import kz.ssss.filo.exception.minio.InvalidPathException;
import kz.ssss.filo.exception.minio.StorageOperationException;
import kz.ssss.filo.repository.MinioRepository;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static kz.ssss.filo.util.Constant.PLACEHOLDER;
import static kz.ssss.filo.util.PathUtil.getPath;
import static kz.ssss.filo.util.PathUtil.getRelativePath;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioRepository minioRepository;
    private final ResourceService resourceService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void createFolder(long userId, String path) {
        initializeBaseFolder(userId);

        if (!PathUtil.isCorrectPath(path)) {
            throw new InvalidPathException("Path is empty or incorrect");
        }

        String fullPath = PathUtil.getFullPath(userId, path);

        if (minioRepository.isObjectExists(bucketName, fullPath, false)) {
            throw new DuplicateResourceException("folder or file with name %s already exists!".formatted(PathUtil.getName(fullPath)));
        }

        createPlaceholder(fullPath);
        log.info("Created new folder with path {}", fullPath);
    }

    public void initializeBaseFolder(long userId) {
        String basePath = PathUtil.getFullPath(userId, "");
        if (!minioRepository.isObjectExists(bucketName, basePath, false)) {
            createPlaceholder(basePath);
            log.info("Created base folder for user with id {}", userId);
        }
    }

    public void downloadFolder(long userId, String path, OutputStream outputStream) {
        String fullPath = PathUtil.getFullPath(userId, path);
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            List<Item> items = minioRepository.listObjects(bucketName, fullPath, true);
            byte[] buffer = new byte[4096];
            for (Item item : items) {
                String fileName = item.objectName();
                String relativePath = getRelativePath(fullPath, fileName);
                if(Objects.equals(PathUtil.getName(relativePath), PLACEHOLDER)){
                    relativePath = getPath(relativePath);
                }
                try (InputStream is = minioRepository.download(bucketName, fileName)) {
                    ZipEntry zipEntry = new ZipEntry(relativePath);
                    zos.putNextEntry(zipEntry);
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) > 0){
                        zos.write(buffer, 0, bytesRead);
                    }
                    zos.closeEntry();
                }
            }
        }catch (Exception e){
            log.error("Occurred error while downloading folder with path {}", fullPath);
            throw new StorageOperationException("Failed to create ZIP archive", e);
        }
    }

    public List<FolderInfoResponse> getAvailableDestinationFolders(long userId, String excludedFolderPath) {
        List<ObjectsInfoResponse> allFolders = resourceService.getResources(userId, "", true, true);
        String objectName = PathUtil.getName(excludedFolderPath);
        return allFolders.stream()
                .map(folder -> new FolderInfoResponse(getPath(folder.path())))
                .distinct()
                .filter(folder -> PathUtil.isValidDestinationFolder(folder.path(), excludedFolderPath))
                .filter(folder -> {
                    String fullPath = PathUtil.getFullPath(userId, folder.path() + objectName);
                    return !minioRepository.isObjectExists(bucketName, fullPath, false);
                })
                .collect(Collectors.toList());
    }

    public void initializeEmptyFolders(String path){
        String parentPath = PathUtil.getParentPath(path);
        String placeholderPath = parentPath + PLACEHOLDER;
        if(!minioRepository.isObjectExists(bucketName, placeholderPath, false)){
            createPlaceholder(parentPath);
            initializeEmptyFolders(parentPath);
        }
    }

    private void createPlaceholder(String basePath) {
        String placeholderPath = basePath + PLACEHOLDER;
        minioRepository.createFolder(bucketName, placeholderPath, new ByteArrayInputStream(new byte[0]), 0);
    }

}

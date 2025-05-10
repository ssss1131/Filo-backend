package kz.ssss.filo.service;

import kz.ssss.filo.exception.minio.DuplicateResourceException;
import kz.ssss.filo.exception.minio.InvalidPathException;
import kz.ssss.filo.exception.minio.StorageOperationException;
import kz.ssss.filo.mapper.ObjectsInfoMapper;
import kz.ssss.filo.repository.MinioRepository;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioRepository minioRepository;
    private final FolderService folderService;
    private final UserQuotaService userQuotaService;
    private final ObjectsInfoMapper mapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void upload(long userId, String path, MultipartFile file) {
        if (!PathUtil.isCorrectPath(path)) {
            throw new InvalidPathException("Path is empty or incorrect");
        }

        folderService.initializeBaseFolder(userId);

        String fullPath = PathUtil.getFullPath(userId, path + file.getOriginalFilename());
        if (minioRepository.isObjectExists(bucketName, fullPath, false)) {
            throw new DuplicateResourceException("folder or file with name %s already exists!".formatted(PathUtil.getName(fullPath)));
        }

        try {
            minioRepository.save(bucketName, fullPath, file.getInputStream(), file.getSize(), file.getContentType());
            log.info("Upload new file with path: {}", fullPath);
        } catch (IOException e) {
            log.error("Occurred exception while getting input stream of file ", e);
            throw new StorageOperationException("Error accessing file stream for " + file.getOriginalFilename(), e);
        }
        folderService.initializeEmptyFolders(fullPath);
        userQuotaService.changeUsedSpaceOnUpload(userId, file.getSize());

    }

    public Resource downloadFile(long userId, String path) {
        String fullPath = PathUtil.getFullPath(userId, path);
        if (!minioRepository.isObjectExists(bucketName, fullPath, false)) {
            throw new InvalidPathException("file with such path does not exist");
        }
        return new InputStreamResource(minioRepository.download(bucketName, fullPath));
    }
}

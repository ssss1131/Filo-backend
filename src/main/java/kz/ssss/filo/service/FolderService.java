package kz.ssss.filo.service;

import kz.ssss.filo.dto.request.CreateFolderRequest;
import kz.ssss.filo.exception.DuplicateResourceException;
import kz.ssss.filo.repository.MinioRepository;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

import static kz.ssss.filo.util.Constant.PLACEHOLDER;
import static kz.ssss.filo.util.PathUtil.FOLDER_DELIMITER;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioRepository minioRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void createFolder(Long userId, String folderName, String path) {
        initializeBaseFolder(userId);
        String fullPath = PathUtil.getFullPath(userId, path + folderName + FOLDER_DELIMITER);
        String placeholderPath = fullPath + PLACEHOLDER;

        if (minioRepository.isObjectExists(bucketName, fullPath, false)) {
            throw new DuplicateResourceException("Folder or file with such name already exists!");
        }

        minioRepository.createFolder(bucketName, placeholderPath, new ByteArrayInputStream(new byte[0]), 0);
        log.info("Created new folder with path {}", fullPath);
    }

    public void createFolder(CreateFolderRequest dto) {
        createFolder(dto.getUserId(), dto.getName(), dto.getPath());
    }

    public void initializeBaseFolder(Long userId) {
        String basePath = PathUtil.getFullPath(userId, "");
        String placeholderPath = basePath + PLACEHOLDER;
        if (!minioRepository.isObjectExists(bucketName, basePath, false)) {
            minioRepository.createFolder(bucketName, placeholderPath, new ByteArrayInputStream(new byte[0]), 0);
            log.info("Created base folder for user with id {}", userId);
        }
    }

}

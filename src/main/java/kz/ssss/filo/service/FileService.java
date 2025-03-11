package kz.ssss.filo.service;

import io.minio.messages.Item;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.exception.DuplicateResourceException;
import kz.ssss.filo.exception.StorageOperationException;
import kz.ssss.filo.mapper.ObjectsInfoMapper;
import kz.ssss.filo.repository.MinioRepository;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static kz.ssss.filo.util.Constant.PLACEHOLDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioRepository minioRepository;
    private final FolderService folderService;
    private final ObjectsInfoMapper mapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public List<ObjectsInfoResponse> getAllObjects(Long userId, String path) {
        String fullPath = PathUtil.getFullPath(userId, path);
        List<Item> items = minioRepository.listObjects(bucketName, fullPath);
        return items.stream()
                .map(item -> mapper.toDto(item, userId))
                .filter(item -> !PathUtil.removePathFromName(item.getName()).equals(PLACEHOLDER))
                .collect(toList());
    }

    public void upload(Long userId, String path, MultipartFile file) {
        folderService.initializeBaseFolder(userId);
        String fullPath = PathUtil.getFullPath(userId, path + file.getOriginalFilename());
        if (minioRepository.isObjectExists(bucketName, fullPath, false)) {
            throw new DuplicateResourceException("file with such name already exists!");
        }
        try {
            minioRepository.save(bucketName, fullPath, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (IOException e) {
            log.error("Occurred exception while getting input stream of file ", e);
            throw new StorageOperationException("Error accessing file stream for " + file.getOriginalFilename(), e);
        }
    }

}

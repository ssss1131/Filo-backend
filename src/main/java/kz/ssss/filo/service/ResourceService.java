package kz.ssss.filo.service;

import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.exception.minio.InvalidPathException;
import kz.ssss.filo.exception.minio.ResourceNotFoundException;
import kz.ssss.filo.mapper.ObjectsInfoMapper;
import kz.ssss.filo.repository.MinioRepository;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static kz.ssss.filo.util.Constant.PLACEHOLDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final MinioRepository minioRepository;
    private final FolderService folderService;
    private final ObjectsInfoMapper mapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public List<ObjectsInfoResponse> getResources(long userId, String path) {
        if (!PathUtil.isCorrectPath(path)) {
            throw new InvalidPathException("Path is empty or incorrect");
        }
        String fullPath = PathUtil.getFullPath(userId, path);
        List<Item> items = minioRepository.listObjects(bucketName, fullPath, false);
        if (items.isEmpty()) {
            log.info("The resource on the {} was not found", path);
            throw new ResourceNotFoundException("The resource on the specified path was not found");
        }
        return items.stream()
                .map(item -> mapper.toDto(item, userId))
                .filter(item -> !PathUtil.getName(item.name()).equals(PLACEHOLDER))
                .collect(toList());
    }


    public List<ObjectsInfoResponse> move(long userId, String from, String to){
        return null;
    }

    public void delete(long userId, String path){
        String fullPath = PathUtil.getFullPath(userId, path);
        List<DeleteObject> objects = minioRepository.listObjects(bucketName, fullPath, true).stream()
                .map(resource -> new DeleteObject(resource.objectName()))
                .toList();
        minioRepository.removeObjects(bucketName, objects);
        log.info("Deleted element with path {}", fullPath);
    }



}

package kz.ssss.filo.service;

import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.exception.minio.InvalidPathException;
import kz.ssss.filo.exception.minio.ResourceNotFoundException;
import kz.ssss.filo.mapper.ObjectsInfoMapper;
import kz.ssss.filo.model.Tenant;
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
    private final ObjectsInfoMapper mapper;

    @Value("${minio.bucket-name}")
    private String bucketName;
//
//    public List<ObjectsInfoResponse> getResources(long userId, String path, boolean isRecursive, boolean havePlaceholders){
//        if (!PathUtil.isCorrectPath(path)) {
//            log.info("Invalid path is {}", path);
//            throw new InvalidPathException("Path is empty or incorrect");
//        }
//        String fullPath = PathUtil.getFullPath(userId, path);
//        List<Item> items = minioRepository.listObjects(bucketName, fullPath, isRecursive);
//        if (items.isEmpty()) {
//            log.info("The resource on the {} was not found", path);
//            throw new ResourceNotFoundException("The resource on the specified path was not found");
//        }
//        return items.stream()
//                .map(item -> mapper.toDto(item, userId))
//                .filter(item -> havePlaceholders || !PathUtil.getName(item.name()).equals(PLACEHOLDER))
//                .collect(toList());
//    }
//
//    public List<ObjectsInfoResponse> getResourcesInFolder(Tenant userId, String path){
//        return getResources(userId, path, false, false);
//    }
//
//
//
//    public List<ObjectsInfoResponse> move(long userId, String from, String to) {
//        String fullFromPath = PathUtil.getFullPath(userId, from);
//        String fullToPath = PathUtil.getFullPath(userId, to);
//
//        List<Item> items = minioRepository.listObjects(bucketName, fullFromPath, true);
//        for (Item item : items) {
//            String oldKey = item.objectName();
//            String newKey = fullToPath + oldKey.substring(fullFromPath.length());
//            minioRepository.copyObject(bucketName, oldKey, newKey);
//        }
//
//        delete(userId, from);
//        return getResourcesInFolder(userId, PathUtil.getPath(to));
//    }
//
//
//    public void delete(long userId, String path){
//        String fullPath = PathUtil.getFullPath(userId, path);
//        List<DeleteObject> objects = minioRepository.listObjects(bucketName, fullPath, true).stream()
//                .map(resource -> new DeleteObject(resource.objectName()))
//                .toList();
//
//        minioRepository.removeObjects(bucketName, objects);
//        log.info("Deleted element with path {}", fullPath);
//    }


}

package kz.ssss.filo.repository;

import io.minio.*;
import io.minio.messages.Item;
import kz.ssss.filo.exception.StorageOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MinioRepository {

    private final MinioClient minioClient;


    public void createFolder(String bucketName, String path, InputStream stream, long objectSize) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(stream, objectSize, -1)
                    .build());
        } catch (Exception e) {
            log.error("Failed to create folder in path '{}' in bucket '{}'", path, bucketName, e);
            throw new StorageOperationException("Failed to create folder in path: " + path, e);
        }
    }

    public void save(String bucketName, String path, InputStream stream, long objectSize, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(stream, objectSize, -1)
                    .contentType(contentType)
                    .build());
            log.info("Saved file with path {}", path);
        } catch (Exception e){
            log.error("Failed to save file with path {} in bucket {}", path, bucketName, e);
            throw new StorageOperationException("Failed to save file in path: " + path, e);
        }
    }

    public boolean isObjectExists(String bucketName, String prefix, boolean isRecursively) {
        Iterable<Result<Item>> fileExists = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(isRecursively)
                .build());
        if (fileExists.iterator().hasNext()) {
            return true;
        }
        Iterable<Result<Item>> folderExists = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(isRecursively)
                .build());
        return folderExists.iterator().hasNext();

    }


    public List<Item> listObjects(String bucketName, String prefix) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .build()
            );

            List<Item> items = new ArrayList<>();
            for (Result<Item> result : results) {
                items.add(result.get());
            }
            return items;

        } catch (Exception e) {
            throw new StorageOperationException("Failed to list objects", e);
        }
    }
}

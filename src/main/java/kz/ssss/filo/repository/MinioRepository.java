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


    public void save(String bucketName, String path, InputStream stream, long objectSize, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(stream, objectSize, -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.error("Failed to save file with path {} in bucket {}", path, bucketName, e);
            throw new StorageOperationException("Failed to save file in path: " + path, e);
        }
    }

    public InputStream download(String bucketName, String path) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build());
        } catch (Exception e) {
            throw new StorageOperationException("Failed to download file with path: " + path, e);
        }
    }

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

    public boolean isObjectExists(String bucketName, String prefix, boolean isRecursively) {
        Iterable<Result<Item>> fileExists = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(isRecursively)
                .build());
        return fileExists.iterator().hasNext();
    }


    public List<Item> listObjects(String bucketName, String prefix, boolean isRecursive) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(isRecursive)
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

package kz.ssss.filo.repository;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import kz.ssss.filo.exception.minio.StorageOperationException;
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


    public void save(String bucket, String path, InputStream stream, long objectSize, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .stream(stream, objectSize, -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.error("Failed to save file with path {} in bucket {}", path, bucket, e);
            throw new StorageOperationException("Failed to save file in path: " + path, e);
        }
    }

    public InputStream download(String bucket, String path) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build());
        } catch (Exception e) {
            throw new StorageOperationException("Failed to download file with path: " + path, e);
        }
    }

    public void createFolder(String bucket, String path, InputStream stream, long objectSize) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .stream(stream, objectSize, -1)
                    .build());
        } catch (Exception e) {
            log.error("Failed to create folder in path '{}'", path, e);
            throw new StorageOperationException("Failed to create folder in path: " + path, e);
        }
    }

    public boolean isObjectExists(String bucket, String prefix, boolean isRecursively) {
        Iterable<Result<Item>> fileExists = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(prefix)
                .recursive(isRecursively)
                .build());
        return fileExists.iterator().hasNext();
    }


    public List<Item> listObjects(String bucket, String prefix, boolean isRecursive) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
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
            log.error("Failed to list objects with prefix {}", prefix);
            throw new StorageOperationException("Failed to list objects", e);
        }
    }

    public void removeObjects(String bucket, List<DeleteObject> objects) {
        try {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(bucket)
                    .objects(objects)
                    .build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error in deleting object {}; {}", error.objectName(), error.message());
            }
        } catch (Exception e) {
            log.error("Exception while deleting elements", e);
            throw new StorageOperationException("Exception while deleting elements", e);
        }
    }

    public void copyObject(String bucket, String from, String to) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucket)
                    .object(to)
                    .source(CopySource.builder()
                            .bucket(bucket)
                            .object(from)
                            .build())
                    .build());
        } catch (Exception e){
            log.error("Exception while copying from {} elements to {}", from, to, e);
            throw new StorageOperationException("Exception while copying elements", e);
        }
    }

    public void createBucket(String bucket){
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if(!bucketExists){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Bucket '{}' successfully created.", bucket);
            }
        }  catch (Exception e) {
            log.error("Error initializing MinIO bucket '{}'.", bucket, e);
            throw new StorageOperationException("Error initializing MinIO bucket", e);
        }
    }
}

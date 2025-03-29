package kz.ssss.filo.exception.minio;

public class StorageOperationException extends RuntimeException {

    public StorageOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

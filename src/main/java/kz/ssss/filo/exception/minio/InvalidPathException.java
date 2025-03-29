package kz.ssss.filo.exception.minio;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException(String message) {
        super(message);
    }
}

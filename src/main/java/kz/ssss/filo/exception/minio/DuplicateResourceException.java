package kz.ssss.filo.exception.minio;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String s) {
        super(s);
    }
}

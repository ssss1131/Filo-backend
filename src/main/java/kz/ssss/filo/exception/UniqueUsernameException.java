package kz.ssss.filo.exception;

public class UniqueUsernameException extends RuntimeException {

    public UniqueUsernameException(String message) {
        super(message);
    }
}

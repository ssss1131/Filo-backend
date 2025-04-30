package kz.ssss.filo.exception.auth;

public class UniqueUsernameException extends RuntimeException {

    public UniqueUsernameException(String message) {
        super(message);
    }
}

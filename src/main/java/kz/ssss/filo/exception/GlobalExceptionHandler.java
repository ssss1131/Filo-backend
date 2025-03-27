package kz.ssss.filo.exception;

import kz.ssss.filo.dto.response.ErrorResponse;
import kz.ssss.filo.dto.response.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ValidationErrorResponse("Validation Exception", errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UniqueUsernameException.class)
    @ResponseBody
    public ErrorResponse handleUniqueUsernameException(UniqueUsernameException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class
    })
    @ResponseBody
    public ErrorResponse handleAuthExceptions() {
        return new ErrorResponse("Invalid username or password");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StorageOperationException.class)
    @ResponseBody
    public ErrorResponse handleStorageOperationException(StorageOperationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse handleException(Exception ex){
        log.error("Unhandled exception: ", ex);
        return new ErrorResponse("Something went wrong");
    }

}

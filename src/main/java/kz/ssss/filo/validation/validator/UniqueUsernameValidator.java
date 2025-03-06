package kz.ssss.filo.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kz.ssss.filo.service.UserService;
import kz.ssss.filo.validation.UniqueUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username cannot be null or empty")
                    .addConstraintViolation();
            return false;
        }
        if (userService.isExistingUsername(username.trim())) {
            context.disableDefaultConstraintViolation();;
            context.buildConstraintViolationWithTemplate("Username is already taken")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

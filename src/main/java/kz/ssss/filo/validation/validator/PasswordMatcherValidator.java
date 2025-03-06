package kz.ssss.filo.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kz.ssss.filo.dto.request.SignUpDto;
import kz.ssss.filo.validation.PasswordMatches;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatches, SignUpDto> {

    @Override
    public boolean isValid(SignUpDto user, ConstraintValidatorContext context) {
        if (user.password() == null || !user.password().equals(user.confirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

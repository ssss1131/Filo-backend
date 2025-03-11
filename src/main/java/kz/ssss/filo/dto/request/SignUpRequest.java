package kz.ssss.filo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SignUpRequest(
        @NotBlank(message = "Username cannot be empty or whitespace")
        @Size(min = 3, max = 30, message = "Username must be between 2 and 30 characters")
        String username,

        @NotBlank(message = "password cannot be empty or whitespace")
        @Size(min = 3, max = 60, message = "Password must be between 3 and 60 characters")
        String password
) {
}

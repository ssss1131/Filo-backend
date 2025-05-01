package kz.ssss.filo.dto.request;

public record CreateUserRequest(
        String username,
        String password,
        String role
) { }
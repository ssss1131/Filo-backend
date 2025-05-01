package kz.ssss.filo.dto.response;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String role,
        Instant createdAt
) { }
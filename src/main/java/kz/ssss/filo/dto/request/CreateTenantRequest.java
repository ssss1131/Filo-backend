package kz.ssss.filo.dto.request;

public record CreateTenantRequest(
        String slug,
        String displayName,
        String adminUsername,
        String adminPassword,
        Long quotaMegaBytes
) { }
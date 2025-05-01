package kz.ssss.filo.dto.response;

import kz.ssss.filo.model.Tenant;

import java.time.Instant;
import java.util.UUID;

public record TenantResponse(
        UUID id,
        String slug,
        String displayName,
        long quotMegabytes,
        Instant createdAt,
        Instant updatedAt
) {
    public static TenantResponse fromEntity(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getDisplayName(),
                tenant.getQuotaMegabytes(),
                tenant.getCreatedAt(),
                tenant.getUpdatedAt()
        );
    }
}
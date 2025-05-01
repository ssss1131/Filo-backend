package kz.ssss.filo.util;

import java.util.UUID;

public record AuthenticatedUser(
        long userId,
        UUID tenantId
) {
}

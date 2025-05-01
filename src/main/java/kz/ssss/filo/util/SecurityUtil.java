package kz.ssss.filo.util;

import kz.ssss.filo.config.UserPrincipal;
import kz.ssss.filo.model.Tenant;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public final class SecurityUtil {

    public static AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return new AuthenticatedUser(principal.getId(), principal.getTenant().getId());
        }

        throw new IllegalStateException("User not authenticated");
    }

}

package kz.ssss.filo.service;

import kz.ssss.filo.dto.request.CreateTenantRequest;
import kz.ssss.filo.dto.response.TenantResponse;
import kz.ssss.filo.model.Tenant;
import kz.ssss.filo.model.User;
import kz.ssss.filo.model.UserRole;
import kz.ssss.filo.repository.TenantRepository;
import kz.ssss.filo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    private final PasswordEncoder encoder;

    public TenantResponse createTenantWithAdmin(CreateTenantRequest req) {
        Tenant tenant = Tenant.builder()
                .name(req.slug())
                .displayName(req.displayName())
                .quotaMegabytes(req.quotaMegaBytes() == null ? 500 : req.quotaMegaBytes())
                .build();
        Tenant savedTenant = tenantRepository.save(tenant);

        User user = User.builder()
                .username(req.adminUsername())
                .password(encoder.encode(req.adminPassword()))
                .tenant(savedTenant)
                .role(UserRole.TENANT_ADMIN)
                .build();

        userRepository.save(user);
        return TenantResponse.fromEntity(savedTenant);
    }
}

package kz.ssss.filo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kz.ssss.filo.config.UserPrincipal;
import kz.ssss.filo.dto.request.SignInRequest;
import kz.ssss.filo.dto.request.SignUpRequest;
import kz.ssss.filo.dto.response.SignUpResponse;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.exception.auth.UniqueUsernameException;
import kz.ssss.filo.model.Tenant;
import kz.ssss.filo.model.User;
import kz.ssss.filo.model.UserRole;
import kz.ssss.filo.repository.TenantRepository;
import kz.ssss.filo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    private final PasswordEncoder encoder; 

    public UserInfoResponse login(SignInRequest req, HttpServletRequest servletReq) {
        Tenant tenant = tenantRepository.findByName(req.tenant())
                .orElseThrow(() -> new BadCredentialsException("Unknown tenant"));

        User user = userRepository.findByUsernameAndTenant(req.username(), tenant)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        UserPrincipal principal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = servletReq.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return new UserInfoResponse(user.getUsername());
    }

    public SignUpResponse registerSuperAdmin(SignUpRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new UniqueUsernameException("Username is already taken");
        }
        User u = User.builder()
                .username(req.username().trim())
                .password(encoder.encode(req.password()))
                .role(UserRole.SUPER_ADMIN)
                .tenant(null)
                .build();
        userRepository.save(u);
        return new SignUpResponse(u.getUsername());
    }
}


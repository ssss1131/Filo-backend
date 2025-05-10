package kz.ssss.filo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kz.ssss.filo.dto.request.SignInRequest;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.exception.auth.UniqueUsernameException;
import kz.ssss.filo.model.User;
import kz.ssss.filo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FolderService folderService;
    private final UserQuotaService userQuotaService;

    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public UserInfoResponse login(SignInRequest signInRequest, HttpServletRequest request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password());
        Authentication authenticate = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return new UserInfoResponse(authenticate.getName());
    }

    @Transactional
    public void save(String username, String rawPassword) {
        if(isExistingUsername(username.trim())){
            throw new UniqueUsernameException("Username is already taken");
        }

        User user = new User(username, encoder.encode(rawPassword));
        userRepository.save(user);
        Long id = user.getId();
        folderService.initializeBaseFolder(id);
        userQuotaService.initializeQuota(id);
    }

    @Transactional(readOnly = true)
    public boolean isExistingUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}

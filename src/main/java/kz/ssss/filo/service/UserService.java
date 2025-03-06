package kz.ssss.filo.service;

import kz.ssss.filo.model.User;
import kz.ssss.filo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public boolean isExistingUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void save(String username, String rawPassword) {
        User user = new User(username, encoder.encode(rawPassword));
        userRepository.save(user);
    }
}

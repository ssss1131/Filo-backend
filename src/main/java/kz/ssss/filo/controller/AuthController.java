package kz.ssss.filo.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.ssss.filo.dto.request.SignInRequest;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kz.ssss.filo.util.Constant.*;

@RestController
@RequestMapping(BASE_AUTH_URL)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<?> login(@RequestBody SignInRequest signInRequest, HttpServletRequest request){
        UserInfoResponse user = authService.login(signInRequest, request);

        return ResponseEntity.ok(user);
    }
}

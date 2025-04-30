package kz.ssss.filo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kz.ssss.filo.dto.request.SignInRequest;
import kz.ssss.filo.dto.request.SignUpRequest;
import kz.ssss.filo.dto.response.SignUpResponse;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kz.ssss.filo.util.Constant.*;

@RestController
@RequestMapping(BASE_AUTH_URL)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<?> login(@RequestBody SignInRequest signInRequest, HttpServletRequest request){
        UserInfoResponse user = userService.login(signInRequest, request);

        return ResponseEntity.ok(user);
    }

    @PostMapping(REGISTER_ENDPOINT)
    public ResponseEntity<?> registration(@Valid @RequestBody SignUpRequest user) {
        userService.save(user.username(), user.password());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SignUpResponse(user.username()));
    }

}

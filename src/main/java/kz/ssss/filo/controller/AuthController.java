package kz.ssss.filo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Auth", description = "User registration and login methods")
@RestController
@RequestMapping(BASE_AUTH_URL)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "User login",
            description = "Accepts SignInRequest, returns UserInfoResponse and establishes session",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful authentication",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<?> login(@RequestBody SignInRequest signInRequest, HttpServletRequest request){
        UserInfoResponse user = userService.login(signInRequest, request);

        return ResponseEntity.ok(user);
    }


    @Operation(
            summary = "User registration",
            description = "Creates a new user with provided username and password"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "User with such username exists")
    })
    @PostMapping(REGISTER_ENDPOINT)
    public ResponseEntity<?> registration(@Valid @RequestBody SignUpRequest user) {
        userService.save(user.username(), user.password());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SignUpResponse(user.username()));
    }

}

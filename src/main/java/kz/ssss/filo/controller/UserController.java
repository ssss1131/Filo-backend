package kz.ssss.filo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ssss.filo.config.UserPrincipal;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.dto.response.UserQuotaResponse;
import kz.ssss.filo.model.UserQuota;
import kz.ssss.filo.service.UserQuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name        = "User",
        description = "Endpoints for retrieving current user info and quota"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserQuotaService userQuotaService;

    @Operation(
            summary     = "Get current user",
            description = "Returns information about the authenticated user",
            security    = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Authenticated user info",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = UserInfoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description  = "Unauthorized",
                    content      = @Content
            )
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserInfoResponse userInfo = new UserInfoResponse(user.getUsername());
        return ResponseEntity.ok(userInfo);
    }

    @Operation(
            summary     = "Get user quota",
            description = "Returns used and total storage quota for the authenticated user",
            security    = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "User quota info",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = UserQuotaResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description  = "Unauthorized",
                    content      = @Content
            )
    })
    @GetMapping("/quota")
    public ResponseEntity<?> getUserQuota(@AuthenticationPrincipal UserPrincipal user){
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserQuota userQuota = userQuotaService.getUserQuota(user.getId());
        UserQuotaResponse userQuotaResponse = new UserQuotaResponse(userQuota.getUsedBytes(), userQuota.getQuotaBytes());
        return ResponseEntity.ok(userQuotaResponse);
    }

}

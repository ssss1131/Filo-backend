package kz.ssss.filo.controller;

import kz.ssss.filo.config.UserPrincipal;
import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.dto.response.UserQuotaResponse;
import kz.ssss.filo.model.UserQuota;
import kz.ssss.filo.service.UserQuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserQuotaService userQuotaService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserInfoResponse userInfo = new UserInfoResponse(user.getUsername());
        return ResponseEntity.ok(userInfo);
    }

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

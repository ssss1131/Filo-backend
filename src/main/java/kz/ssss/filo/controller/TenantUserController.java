package kz.ssss.filo.controller;

import kz.ssss.filo.dto.request.CreateUserRequest;
import kz.ssss.filo.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/users")
@RequiredArgsConstructor
public class TenantUserController {

//    private final UserService userService;
//
//    /**
//     * Список всех пользователей данного тенанта.
//     */
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> listUsers(
//            @PathVariable UUID tenantId
//    ) {
//        List<UserResponse> list = userService.getUsersByTenant(tenantId);
//        return ResponseEntity.ok(list);
//    }
//
//    /**
//     * Создать нового пользователя внутри тенанта.
//     * Доступно только TENANT_ADMIN-у этого тенанта.
//     */
//    @PostMapping
//    public ResponseEntity<UserResponse> createUser(
//            @PathVariable UUID tenantId,
//            @RequestBody CreateUserRequest req
//    ) {
//        UserResponse dto = userService.createUserInTenant(
//                tenantId,
//                req.username(),
//                req.password(),
//                req.role()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }

    /**
     * Удалить пользователя из тенанта.
     */
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> deleteUser(
//            @PathVariable UUID tenantId,
//            @PathVariable Long userId
//    ) {
//        userService.deleteUser(tenantId, userId);
//        return ResponseEntity.noContent().build();
//    }
}
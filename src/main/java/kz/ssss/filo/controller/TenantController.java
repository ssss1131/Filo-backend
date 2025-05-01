package kz.ssss.filo.controller;

import kz.ssss.filo.dto.request.CreateTenantRequest;
import kz.ssss.filo.dto.response.TenantResponse;
import kz.ssss.filo.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @RequestBody CreateTenantRequest req) {
        TenantResponse dto = tenantService.createTenantWithAdmin(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}

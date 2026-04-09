package fiveonestudy.ddait.myPage.controller;

import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.myPage.dto.CertificationResponse;
import fiveonestudy.ddait.myPage.service.AdminCertificationService;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/certifications")
public class AdminCertificationController {

    private final AdminCertificationService adminCertificationService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getPendingList(
            @AuthenticationPrincipal User admin
    ) {

        validateAdmin(admin);

        List<CertificationResponse> result =
                adminCertificationService.getPendingList()
                        .stream()
                        .map(CertificationResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin
    ) {

        validateAdmin(admin);

        adminCertificationService.approve(id, admin);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User admin
    ) {

        validateAdmin(admin);

        String reason = request.get("reason");

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        adminCertificationService.reject(id, admin, reason);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private void validateAdmin(User admin) {
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("FORBIDDEN");
        }
    }
}
package fiveonestudy.ddait.myPage.controller;

import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.myPage.dto.CertificationRequest;
import fiveonestudy.ddait.myPage.dto.CertificationResponse;
import fiveonestudy.ddait.myPage.service.UserCertificationService;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certifications")
public class UserCertificationController {

    private final UserCertificationService userCertificationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> register(
            @RequestPart("data") CertificationRequest request,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws Exception {

        if (user == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        Long id = userCertificationService.register(user, request, file);

        return ResponseEntity.ok(
                ApiResponse.success(Map.of("name", request.getName()))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getMyCertifications(
            @AuthenticationPrincipal User user
    ) {

        if (user == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        List<CertificationResponse> result =
                userCertificationService.getMyCertifications(user.getId())
                        .stream()
                        .map(CertificationResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
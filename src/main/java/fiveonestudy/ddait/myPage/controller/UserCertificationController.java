package fiveonestudy.ddait.myPage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.myPage.dto.CertificationListResponse;
import fiveonestudy.ddait.myPage.dto.CertificationRequest;
import fiveonestudy.ddait.myPage.dto.CertificationResponse;
import fiveonestudy.ddait.myPage.entity.CertificationFile;
import fiveonestudy.ddait.myPage.repository.CertificationFileRepository;
import fiveonestudy.ddait.security.CustomUserDetails;
import fiveonestudy.ddait.myPage.service.UserCertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/certifications")
public class UserCertificationController {

    private final UserCertificationService userCertificationService;
    private final ObjectMapper objectMapper;
    private final CertificationFileRepository certificationFileRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> register(
            @RequestPart("data") String data,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {

        if (userDetails == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        CertificationRequest request = objectMapper.readValue(data, CertificationRequest.class);

        userCertificationService.register(userDetails.getUser(), request, file);

        return ResponseEntity.ok(
                ApiResponse.success(Map.of("name", request.getName()))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getMyCertifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        List<CertificationResponse> result =
                userCertificationService.getMyCertifications(userDetails.getId())
                        .stream()
                        .map(CertificationResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        userCertificationService.delete(userDetails.getId(), id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CertificationListResponse>>> getAllCertifications() {

        List<CertificationListResponse> result =
                userCertificationService.getAllCertifications()
                        .stream()
                        .map(CertificationListResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getCertificationFile(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        CertificationFile file = certificationFileRepository.findByUserCertificationId(id)
                .orElseThrow(()-> new RuntimeException("FILE_NOT_FOUND"));

        if (!file.getUserCertification().getUser().getId().equals(userDetails.getId())
                && !userDetails.getUser().isAdmin()) {
            throw new RuntimeException("FORBIDDEN");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getOriginalName() + "\"")
                .body(file.getFileData());
    }
}
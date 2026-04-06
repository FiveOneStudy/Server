package fiveonestudy.ddait.myPage.controller;

import fiveonestudy.ddait.myPage.security.CustomUserDetails;
import fiveonestudy.ddait.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
class MyPgeController {

    private final MyPageService mypageService;

    @PatchMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            String imageUrl = mypageService.updateProfileImage(userDetails.getId(), image);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of("imageUrl", imageUrl),
                    "error", null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "data", null,
                    "error", Map.of(
                            "code", "INVALID_FILE",
                            "message", e.getMessage()
                    )
            ));
        }
    }
}
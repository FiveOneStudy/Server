package fiveonestudy.ddait.myPage.controller;

import fiveonestudy.ddait.myPage.security.CustomUserDetails;
import fiveonestudy.ddait.myPage.service.MyPageService;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService mypageService;
    private final UserRepository userRepository;

    @PatchMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            String fileName = mypageService.updateProfileImage(userDetails.getId(), image);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of("imageUrl", "/mypage/profile-image/view"),
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

    @GetMapping("/profile-image/view")
    public ResponseEntity<Resource> getProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        Path filePath = Paths.get("uploads/profile-images").resolve(user.getImageUrl());
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(resource);
    }
}
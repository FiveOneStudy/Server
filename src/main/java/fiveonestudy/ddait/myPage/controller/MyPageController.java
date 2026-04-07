package fiveonestudy.ddait.myPage.controller;

import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.myPage.dto.NicknameResponse;
import fiveonestudy.ddait.myPage.dto.ProfileImageResponse;
import fiveonestudy.ddait.myPage.security.CustomUserDetails;
import fiveonestudy.ddait.myPage.service.MyPageService;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // ← 수정됨
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService mypageService;
    private final UserRepository userRepository;

    @PatchMapping("/profile-image")
    public ResponseEntity<ApiResponse<ProfileImageResponse>> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("image") MultipartFile image
    ) {
        mypageService.updateProfileImage(userDetails.getId(), image);

        return ResponseEntity.ok(
                ApiResponse.success(new ProfileImageResponse("/mypage/profile-image"))
        );
    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(
            @PathVariable Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        if (user.getProfileImage() == null) {
            throw new IllegalArgumentException("PROFILE_IMAGE_NOT_FOUND");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, user.getProfileImageType())
                .body(user.getProfileImage());
    }


    @PatchMapping("/profile-nickname")
    public ResponseEntity<ApiResponse<NicknameResponse>> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> body
    ) {
        try {
            String nickname = body.get("nickname");
            String updated = mypageService.updateNickname(userDetails.getId(), nickname);

            return ResponseEntity.ok(
                    ApiResponse.success(new NicknameResponse(updated))
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail("NICKNAME_DUPLICATE", "이미 사용 중인 닉네임입니다.")
            );
        }
    }
}
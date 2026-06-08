package fiveonestudy.ddait.user.controller;

import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.jwt.dto.TokenDto;
import fiveonestudy.ddait.user.dto.*;
import fiveonestudy.ddait.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<TokenDto> signUp(@RequestBody @Validated UserSignUpDto dto) {

        return ResponseEntity.ok(userService.signUp(dto));
    }

    @PostMapping("/password/reset/code")
    public ResponseEntity<ApiResponse<PasswordResetResponse>> sendPasswordResetCode(
            @RequestBody @Validated PasswordResetCodeRequest dto
    ) {
        userService.sendResetCode(dto);

        return ResponseEntity.ok(
                ApiResponse.success(new PasswordResetResponse("인증번호가 이메일로 발송되었습니다."))
        );
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<ApiResponse<PasswordResetResponse>> resetPassword(
            @RequestBody @Validated PasswordResetDto dto
    ) {
        userService.resetPassword(dto);

        return ResponseEntity.ok(
                ApiResponse.success(new PasswordResetResponse("비밀번호가 재설정되었습니다."))
        );
    }
}
package fiveonestudy.ddait.user.controller;

import fiveonestudy.ddait.user.dto.UserSignUpDto;
import fiveonestudy.ddait.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody @Validated UserSignUpDto dto) {
        try {
            userService.signUp(dto);
            return ResponseEntity.status(205).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package fiveonestudy.ddait.user.controller;

import fiveonestudy.ddait.jwt.dto.TokenDto;
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

    @PostMapping("/signup")
    public ResponseEntity<TokenDto> signUp(@RequestBody @Validated UserSignUpDto dto) {

        return ResponseEntity.ok(userService.signUp(dto));
    }
}
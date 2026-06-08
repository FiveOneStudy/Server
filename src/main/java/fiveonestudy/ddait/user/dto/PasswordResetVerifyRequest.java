package fiveonestudy.ddait.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordResetVerifyRequest {

    private String email;
    private String code;
}
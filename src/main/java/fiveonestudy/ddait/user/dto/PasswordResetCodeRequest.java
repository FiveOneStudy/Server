package fiveonestudy.ddait.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordResetCodeRequest {

    private String email;
}
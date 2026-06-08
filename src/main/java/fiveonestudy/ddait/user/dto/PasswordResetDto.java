package fiveonestudy.ddait.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordResetDto {
    private String email;
    private String newPassword;
}

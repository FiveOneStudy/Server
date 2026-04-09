package fiveonestudy.ddait.myPage.dto;

import fiveonestudy.ddait.myPage.entity.UserCertification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CertificationResponse {

    private Long id;
    private String name;
    private String issuer;
    private LocalDate acquiredDate;
    private String status;

    public static CertificationResponse from(UserCertification uc) {
        return CertificationResponse.builder()
                .id(uc.getId())
                .name(uc.getCertification().getName())
                .issuer(uc.getCertification().getIssuer())
                .acquiredDate(uc.getAcquiredDate())
                .status(uc.getStatus().name())
                .build();
    }
}
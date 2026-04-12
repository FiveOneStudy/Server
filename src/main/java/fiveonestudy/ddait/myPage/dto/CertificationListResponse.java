package fiveonestudy.ddait.myPage.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import fiveonestudy.ddait.myPage.entity.Certification;

@Getter
@Builder
@AllArgsConstructor
public class CertificationListResponse {

    private Long id;
    private String name;
    private String issuer;

    public static CertificationListResponse from(Certification certification) {
        return CertificationListResponse.builder()
                .id(certification.getId())
                .name(certification.getName())
                .issuer(certification.getIssuer())
                .build();
    }
}
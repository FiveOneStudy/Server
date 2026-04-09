package fiveonestudy.ddait.myPage.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CertificationRequest {

    private String name;
    private String issuer;
    private LocalDate acquiredDate;
    private String verifiedId;
}
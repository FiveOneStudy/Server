package fiveonestudy.ddait.jwt.dto;


public record TokenDto(
        String accessToken,
        String refreshToken
) {}
package fiveonestudy.ddait.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(String email) {
        Date now = new Date();

        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .expiration(new Date(now.getTime() + accessTokenExpirationPeriod))
                .claim(EMAIL_CLAIM, email)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .expiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("Access Token 발급: {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response,
                                          String accessToken,
                                          String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access / Refresh Token 발급 완료");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.ofNullable(claims.get(EMAIL_CLAIM, String.class));
        } catch (Exception e) {
            log.error("유효하지 않은 Access Token");
            return Optional.empty();
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰: {}", e.getMessage());
            return false;
        }
    }
}
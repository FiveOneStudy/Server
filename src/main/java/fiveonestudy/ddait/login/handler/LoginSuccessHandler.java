package fiveonestudy.ddait.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiveonestudy.ddait.jwt.dto.TokenDto;
import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String email = extractUsername(authentication);

        TokenDto token = jwtService.issueToken(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), token);

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(token.refreshToken());
                    userRepository.saveAndFlush(user);
                });

        log.info("로그인 성공: {}", email);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
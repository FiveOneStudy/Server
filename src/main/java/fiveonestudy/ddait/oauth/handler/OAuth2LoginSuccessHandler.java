package fiveonestudy.ddait.oauth.handler;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.oauth.CustomOAuth2User;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        log.info("OAuth2 Login 성공!");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> loginSuccess(response, email),     // 기존 유저
                        () -> signUpAndLogin(response, oAuth2User) // 신규 유저
                );
    }

    private void signUpAndLogin(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();

        fiveonestudy.ddait.user.entity.User newUser =
                fiveonestudy.ddait.user.entity.User.builder()
                        .email(email)
                        .password(null)
                        .role(Role.USER)
                        .oauthId(oAuth2User.getName())
                        .build();

        userRepository.save(newUser);

        loginSuccess(response, email);
    }

    private void loginSuccess(HttpServletResponse response, String email) {

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    userRepository.saveAndFlush(user);
                });
    }
}
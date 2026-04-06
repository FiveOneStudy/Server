package fiveonestudy.ddait.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiveonestudy.ddait.jwt.dto.TokenDto;
import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.oauth.CustomOAuth2User;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

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

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            loginSuccess(response, email);
        } else {
            signUpAndLogin(response, oAuth2User);
        }
    }

    private void signUpAndLogin(HttpServletResponse response,
                                CustomOAuth2User oAuth2User) throws IOException {

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

    private void loginSuccess(HttpServletResponse response, String email) throws IOException {

        TokenDto token = jwtService.issueToken(email);

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(token.refreshToken());
                    userRepository.saveAndFlush(user);
                });

        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
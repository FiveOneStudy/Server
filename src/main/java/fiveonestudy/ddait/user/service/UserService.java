package fiveonestudy.ddait.user.service;

import fiveonestudy.ddait.jwt.dto.TokenDto;
import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.user.dto.PasswordResetCodeRequest;
import fiveonestudy.ddait.user.dto.PasswordResetDto;
import fiveonestudy.ddait.user.dto.PasswordResetVerifyRequest;
import fiveonestudy.ddait.user.dto.UserSignUpDto;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final String CODE_KEY_PREFIX = "password:reset:code:";
    private static final String VERIFIED_KEY_PREFIX = "password:reset:verified:";
    private static final Duration CODE_EXPIRE_TIME = Duration.ofMinutes(5);
    private static final Duration VERIFIED_EXPIRE_TIME = Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender javaMailSender;

    public TokenDto signUp(UserSignUpDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return jwtService.issueToken(user.getEmail());
    }

    public void sendResetCode(PasswordResetCodeRequest request) {
        String email = request.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        String code = generateCode();

        redisTemplate.opsForValue().set(
                CODE_KEY_PREFIX + email,
                code,
                CODE_EXPIRE_TIME
        );

        redisTemplate.delete(VERIFIED_KEY_PREFIX + email);

        sendMail(email, code);
    }

    public void verifyResetCode(PasswordResetVerifyRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("인증번호를 입력해주세요.");
        }

        String key = CODE_KEY_PREFIX + email;
        Object savedCode = redisTemplate.opsForValue().get(key);

        if (savedCode == null) {
            throw new IllegalArgumentException("인증번호가 만료되었거나 존재하지 않습니다.");
        }

        if (!savedCode.toString().equals(code)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        redisTemplate.opsForValue().set(
                VERIFIED_KEY_PREFIX + email,
                true,
                VERIFIED_EXPIRE_TIME
        );

        redisTemplate.delete(key);
    }

    public void resetPassword(PasswordResetDto request) {
        String email = request.getEmail();
        String code = request.getCode();
        String newPassword = request.getNewPassword();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("인증번호를 입력해주세요.");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("새 비밀번호를 입력해주세요.");
        }

        String key = CODE_KEY_PREFIX + email;
        Object savedCode = redisTemplate.opsForValue().get(key);

        if (savedCode == null) {
            throw new IllegalArgumentException("인증번호가 만료되었거나 존재하지 않습니다.");
        }

        if (!savedCode.toString().equals(code)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        user.updatePassword(passwordEncoder.encode(newPassword));

        redisTemplate.delete(key);
        redisTemplate.delete(VERIFIED_KEY_PREFIX + email);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;

        return String.valueOf(code);
    }

    private void sendMail(String email, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[DDAIT] 비밀번호 재설정 인증번호");
            message.setText(
                    """
                    <div style="font-family: Arial, sans-serif;">
                        <h2>비밀번호 재설정 인증번호</h2>
                        <p>아래 인증번호를 입력해주세요.</p>
                        <h1 style="letter-spacing: 4px;">%s</h1>
                        <p>인증번호는 5분 동안 유효합니다.</p>
                    </div>
                    """.formatted(code),
                    "UTF-8",
                    "html"
            );

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("이메일 발송에 실패했습니다.");
        }
    }
}
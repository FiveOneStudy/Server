package fiveonestudy.ddait.myPage.service;

import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;

    private static final long MAX_FILE_SIZE = 5*1024*1024;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/jpg");

    @Transactional
    public void updateProfileImage(Long userId, MultipartFile image) {

        if (image.isEmpty()) throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        if (!ALLOWED_TYPES.contains(image.getContentType()))
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        if (image.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("파일 크기가 5MB를 초과했습니다.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        try {
            user.updateProfileImage(image.getBytes(), image.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 실패", e);
        }
    }

    @Transactional
    public String updateNickname(Long userId, String nickname){
        if(userRepository.existsByNickname(nickname)){
            throw new IllegalArgumentException("NICKNAME_DUPLICATE");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("유저를 찾을 수 없습니다."));
        user.setNickname(nickname);
        return nickname;
    }

}

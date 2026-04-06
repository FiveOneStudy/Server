package fiveonestudy.ddait.myPage.service;

import com.google.api.client.util.Value;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5*1024*1024;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/jpg");

    public String updateProfileImage(Long userId, MultipartFile image) {
        if(image.isEmpty()) throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        if (!ALLOWED_TYPES.contains(image.getContentType()))
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        if (image.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("파일 크기가 5MB를 초과했습니다.");

        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더 생성 실패", e);
        }

        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        try {
            image.transferTo(filePath.toFile());
        } catch (IOException e){
            throw new RuntimeException("파일 저장 실패", e);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        String imageUrl = "/uploads/profile-images/" + fileName;
        user.setProfileImageUrl(imageUrl);

        return imageUrl;
    }
}

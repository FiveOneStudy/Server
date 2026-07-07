package fiveonestudy.ddait.user.repository;

import fiveonestudy.ddait.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNicknameIn(List<String> nicknames);

    Optional<User> findByNickname(String nickname);

    Optional<User> findFirstByNickname(String nickname);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByid(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
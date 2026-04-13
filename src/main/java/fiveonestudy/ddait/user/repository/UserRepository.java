package fiveonestudy.ddait.user.repository;

import fiveonestudy.ddait.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByid(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
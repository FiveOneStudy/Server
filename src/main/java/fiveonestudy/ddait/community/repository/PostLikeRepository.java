package fiveonestudy.ddait.community.repository;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostLike;
import fiveonestudy.ddait.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<PostLike> findByUserAndPost(User user, Post post);
}

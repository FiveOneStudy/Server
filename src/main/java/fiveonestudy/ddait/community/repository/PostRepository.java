package fiveonestudy.ddait.community.repository;

import fiveonestudy.ddait.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc();
    List<Post> findAllByOrderByLikeCountDesc();
    List<Post> findByUserIdOrderByIdDesc(Long userId);
}

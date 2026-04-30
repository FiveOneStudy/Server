package fiveonestudy.ddait.community.repository;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByStatusOrderByIdDesc(PostStatus status);
    List<Post> findAllByStatusOrderByLikeCountDesc(PostStatus status);
    List<Post> findByUserIdOrderByIdDesc(Long userId);

    @Modifying
    @Query("update Post p set p.viewCount = p.viewCount + :count where p.id = :id")
    void incrementViewCount(@Param("id") Long id, @Param("count") Long count);

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + :count where p.id = :id")
    void incrementLikeCount(@Param("id") Long id, @Param("count") Long count);

    List<Post> findAllByStatus(PostStatus status);
}

package fiveonestudy.ddait.community.repository;

import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByIdAsc(Long postId);
    List<Comment> findByUserIdOrderByIdDesc(Long userId);
    Optional<Comment> findByIdAndPostId(Long postId, Long id);

    List<Comment> findAllByStatus(CommentStatus status);
}
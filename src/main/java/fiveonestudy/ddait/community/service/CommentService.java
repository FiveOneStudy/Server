package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.repository.CommentRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Long create(User user, Long postId, String content, Long parentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Comment parent = null;

        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("부모 댓글 없음"));
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .parent(parent)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByIdAsc(postId);
    }

    public void delete(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("FORBIDDEN");
        }

        comment.softDelete();
    }

    @Transactional(readOnly = true)
    public List<Comment> getMyComments(Long userId) {
        return commentRepository.findByUserId(userId);
    }

}

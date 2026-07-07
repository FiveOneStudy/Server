package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.dto.CreateCommentRequest;
import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.CommentStatus;
import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.repository.CommentRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.global.exception.ForbiddenException;
import fiveonestudy.ddait.global.exception.NotFoundException;
import fiveonestudy.ddait.global.external.openai.ModerationClient;
import fiveonestudy.ddait.global.moderation.dto.ModerationResult;
import fiveonestudy.ddait.global.moderation.service.ModerationService;
import fiveonestudy.ddait.user.entity.Role;
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
    private final ModerationService moderationService;

    public Long create(User user, Long postId, CreateCommentRequest request) {

        String content = request.content().trim();

        ModerationResult result = moderationService.evaluate(content);

        if (result == ModerationResult.BLOCKED) {
            throw new IllegalArgumentException("부적절한 내용입니다.");
        }

        CommentStatus status = (result == ModerationResult.REVIEW)
                ? CommentStatus.PENDING
                : CommentStatus.APPROVED;

        Post post = postRepository.getReferenceById(postId);

        Comment parent = null;

        if (request.parentId() != null) {
            parent = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException("부모 댓글 없음"));

            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalArgumentException("INVALID_PARENT");
            }
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .parent(parent)
                .status(status)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("게시글 없음");
        }

        return commentRepository.findByPostIdOrderByIdAsc(postId);
    }

    public void delete(User user, Long postId, Long commentId) {

        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new NotFoundException("댓글 없음"));

        if (!comment.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException();
        }

        commentRepository.delete(comment);
    }

}
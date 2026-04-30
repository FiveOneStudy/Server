package fiveonestudy.ddait.admin.service;

import fiveonestudy.ddait.admin.dto.CommentAdminResponse;
import fiveonestudy.ddait.admin.dto.PostAdminResponse;
import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.CommentStatus;
import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostStatus;
import fiveonestudy.ddait.community.repository.CommentRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<PostAdminResponse> getPendingPosts() {
        return postRepository.findAllByStatus(PostStatus.PENDING)
                .stream()
                .map(p -> new PostAdminResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getStatus()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentAdminResponse> getPendingComments() {
        return commentRepository.findAllByStatus(CommentStatus.PENDING)
                .stream()
                .map(c -> new CommentAdminResponse(
                        c.getId(),
                        c.getPost().getId(),
                        c.getUser().getId(),
                        c.getContent(),
                        c.getStatus()
                ))
                .toList();
    }

    public void approvePost(Long postId) {
        Post post = getPost(postId);
        post.setStatus(PostStatus.APPROVED);
    }

    public void rejectPost(Long postId) {
        Post post = getPost(postId);
        post.setStatus(PostStatus.REJECTED);
    }

    public void approveComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.setStatus(CommentStatus.APPROVED);
    }

    public void rejectComment(Long commentId) {
        Comment comment = getComment(commentId);
        comment.setStatus(CommentStatus.REJECTED);
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));
    }

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글 없음"));
    }
}
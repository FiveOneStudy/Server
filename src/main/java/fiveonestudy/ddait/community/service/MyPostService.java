package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.repository.CommentRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public List<Post> getMyPosts(Long userId) {
        return postRepository.findByUserIdOrderByIdDesc(userId);
    }

    public List<Comment> getMyComment(Long userId) {
        return commentRepository.findByUserId(userId);
    }
}

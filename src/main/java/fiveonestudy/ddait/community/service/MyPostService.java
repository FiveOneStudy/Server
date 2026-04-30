package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.dto.MyCommentResponse;
import fiveonestudy.ddait.community.dto.MyPostResponse;
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

    public List<MyPostResponse> getMyPosts(Long userId) {
        return postRepository.findByUserIdOrderByIdDesc(userId)
                .stream()
                .map(MyPostResponse::from)
                .toList();
    }

    public List<MyCommentResponse> getMyComments(Long userId) {
        return commentRepository.findByUserIdOrderByIdDesc(userId)
                .stream()
                .map(MyCommentResponse::from)
                .toList();
    }
}

package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostLike;
import fiveonestudy.ddait.community.repository.PostLikeRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.global.exception.ForbiddenException;
import fiveonestudy.ddait.global.exception.NotFoundException;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public Long create(User user, String title, String content) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        Post post = Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .likeCount(0)
                .viewCount(0)
                .build();

        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(String sort) {

        return switch (sort) {
            case "popular" -> postRepository.findAllByOrderByLikeCountDesc();
            case "latest" -> postRepository.findAllByOrderByIdDesc();
            default -> throw new IllegalArgumentException("INVALID_SORT");
        };
    }

    public Post getPost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));

        post.incrementView();
        return post;
    }

    public void delete(User user, Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }

        postRepository.delete(post);
    }

    public void like(User user, Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));

        boolean exists = postLikeRepository.existsByUserAndPost(user, post);

        if (exists) {
            throw new IllegalArgumentException("ALREADY_LIKED");
        }

        postLikeRepository.save(new PostLike(user, post));
        post.incrementLike();
    }

    public void unlike(User user, Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));

        PostLike like = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new NotFoundException("좋아요 없음"));

        postLikeRepository.delete(like);
        post.decrementLike();
    }
}

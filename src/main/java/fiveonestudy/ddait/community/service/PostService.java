package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostLike;
import fiveonestudy.ddait.community.entity.PostSort;
import fiveonestudy.ddait.community.entity.PostStatus;
import fiveonestudy.ddait.community.repository.PostLikeRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.global.exception.ForbiddenException;
import fiveonestudy.ddait.global.exception.NotFoundException;
import fiveonestudy.ddait.global.moderation.dto.ModerationResult;
import fiveonestudy.ddait.global.moderation.service.ModerationService;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostLikeRepository postLikeRepository;
    private final ModerationService moderationService;

    public Long create(User user, String title, String content) {

        String text = title.trim() + " " + content.trim();

        ModerationResult result = moderationService.evaluate(text);

        if (result == ModerationResult.BLOCKED) {
            throw new IllegalArgumentException("부적절한 내용입니다.");
        }

        PostStatus status = (result == ModerationResult.REVIEW)
                ? PostStatus.PENDING
                : PostStatus.APPROVED;

        Post post = Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .likeCount(0)
                .viewCount(0)
                .status(status)
                .build();

        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(PostSort sort) {

        return switch (sort) {
            case POPULAR -> postRepository.findAllByStatusOrderByViewCountDesc(PostStatus.APPROVED);
            case LATEST -> postRepository.findAllByStatusOrderByIdDesc(PostStatus.APPROVED);
        };
    }

    public Post getPost(Long id, User user) {

        Post post = getPostEntity(id);

        // 비로그인 처리 (선택 정책)
        if (user == null) {
            post.incrementViewCount();
            return post;
        }

        String key = "view:" + user.getId() + ":" + id;

        Boolean alreadyViewed = redisTemplate.hasKey(key);

        if (Boolean.FALSE.equals(alreadyViewed)) {

            post.incrementViewCount();

            redisTemplate.opsForValue().set(
                    key,
                    "1",
                    Duration.ofHours(24)
            );
        }

        return post;
    }

    public void delete(User user, Long id) {

        Post post = getPostEntity(id);

        if (!post.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException();
        }

        postRepository.delete(post);
    }

    public void like(User user, Long id) {

        Post post = getPostEntity(id);

        try {
            postLikeRepository.save(new PostLike(user, post));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("ALREADY_LIKED");
        }

        post.incrementLikeCount();
    }

    public void unlike(User user, Long id) {

        Post post = getPostEntity(id);

        PostLike like = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new NotFoundException("좋아요 없음"));

        postLikeRepository.delete(like);
        post.decrementLikeCount();
    }

    private Post getPostEntity(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));
    }

    @Transactional(readOnly = true)
    public Long getNextPostId(Long postId, PostSort sort) {
        Post currentPost = getPostEntity(postId);
        switch (sort){
            case LATEST:
                return postRepository
                        .findFirstByStatusAndIdLessThanOrderByIdDesc(
                                PostStatus.APPROVED,
                                postId
                        )
                        .map(Post::getId)
                        .orElse(null);
            case POPULAR:
                List<Post> posts = postRepository.findAllByStatusOrderByViewCountDesc(PostStatus.APPROVED);
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getId().equals(currentPost.getId())) {

                        return i + 1 < posts.size()
                                ? posts.get(i + 1).getId()
                                : null;
                    }
                }
                return null;

        }
        return null;
    }
}
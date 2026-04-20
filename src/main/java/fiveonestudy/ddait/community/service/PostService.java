package fiveonestudy.ddait.community.service;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.community.entity.PostLike;
import fiveonestudy.ddait.community.repository.PostLikeRepository;
import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public Long create(User user, String title, String content) {
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
        if("popular".equals(sort)) {
            return postRepository.findAllByOrderByLikeCountDesc();
        }
        return postRepository.findAllByOrderByIdDesc();
    }

    public Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("게시글 없음"));
        post.incrementView();
        return post;
    }

    public void delete(User user, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if(!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("FORBIDDEN");
        }
        postRepository.delete(post);
    }

    public boolean like(User user, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Optional<PostLike> existing = postLikeRepository.findByUserAndPost(user, post);

        if(existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            post.decrementLike();
            return false;
        }

        postLikeRepository.save(new PostLike(user, post));
        post.incrementLike();
        return true;
    }
}

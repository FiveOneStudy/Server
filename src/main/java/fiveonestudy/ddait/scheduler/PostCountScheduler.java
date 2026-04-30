package fiveonestudy.ddait.scheduler;

import fiveonestudy.ddait.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PostCountScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void syncViewCount() {

        Set<String> keys = redisTemplate.keys("post:view:*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {

            Long postId = Long.parseLong(key.split(":")[2]);
            String value = redisTemplate.opsForValue().get(key);

            if (value == null) continue;

            Long count = Long.parseLong(value);

            postRepository.incrementViewCount(postId, count);

            redisTemplate.delete(key);
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void syncLikeCount() {

        Set<String> keys = redisTemplate.keys("post:like:*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {

            Long postId = Long.parseLong(key.split(":")[2]);
            String value = redisTemplate.opsForValue().get(key);

            if (value == null) continue;

            Long count = Long.parseLong(value);

            postRepository.incrementLikeCount(postId, count);

            redisTemplate.delete(key);
        }
    }
}
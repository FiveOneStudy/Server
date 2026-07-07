package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.PostStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateResponse {

    private Long postId;
    private PostStatus status;
}
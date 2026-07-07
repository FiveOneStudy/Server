package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.CommentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateResponse {

    private Long commentId;
    private CommentStatus status;
}
package fiveonestudy.ddait.admin.dto;

import fiveonestudy.ddait.community.entity.CommentStatus;

public record CommentAdminResponse(
        Long id,
        Long postId,
        Long userId,
        String content,
        CommentStatus status
) {}
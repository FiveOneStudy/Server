package fiveonestudy.ddait.admin.dto;

import fiveonestudy.ddait.community.entity.PostStatus;

public record PostAdminResponse(
        Long id,
        String title,
        String content,
        PostStatus status) {
}

package fiveonestudy.ddait.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "최대 1000자까지 가능합니다.")
        String content,

        Long parentId
) {}
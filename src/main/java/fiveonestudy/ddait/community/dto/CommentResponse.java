package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private String content;
    private Long userId;
    private String writer;
    private Long parentId;
    private LocalDate createdAt;
    private boolean canDelete;

    public static CommentResponse from(Comment comment) {
        return from(comment, null);
    }

    public static CommentResponse from(Comment comment, User currentUser) {

        boolean canDelete = currentUser != null
                && (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getRole() == Role.ADMIN);

        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .writer(comment.getUser().getNickname())
                .userId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt().toLocalDate())
                .canDelete(canDelete)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .build();
    }
}
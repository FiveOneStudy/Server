package fiveonestudy.ddait.community.controller;

import fiveonestudy.ddait.community.dto.CommentCreateResponse;
import fiveonestudy.ddait.community.dto.CommentResponse;
import fiveonestudy.ddait.community.dto.CreateCommentRequest;
import fiveonestudy.ddait.community.service.CommentService;
import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.security.CustomUserDetails;
import fiveonestudy.ddait.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {

        CommentCreateResponse response = commentService.create(
                userDetails.getUser(),
                postId,
                request
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {

        User currentUser =
                userDetails != null ? userDetails.getUser() : null;

        List<CommentResponse> result =
                commentService.getComments(postId)
                        .stream()
                        .map(comment -> CommentResponse.from(comment, currentUser))
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId,
            @PathVariable Long postId) {

        commentService.delete(userDetails.getUser(),postId, commentId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
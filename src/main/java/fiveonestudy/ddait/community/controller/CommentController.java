package fiveonestudy.ddait.community.controller;

import fiveonestudy.ddait.community.dto.CommentResponse;
import fiveonestudy.ddait.community.dto.CreateCommentRequest;
import fiveonestudy.ddait.community.service.CommentService;
import fiveonestudy.ddait.global.exception.UnauthorizedException;
import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.security.CustomUserDetails;
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
    public ResponseEntity<ApiResponse<Map<String, Long>>> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {

        Long id = commentService.create(
                userDetails.getUser(),
                postId,
                request
        );

        return ResponseEntity.ok(ApiResponse.success(Map.of("commentId", id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long postId
    ) {
        List<CommentResponse> result =
                commentService.getComments(postId)
                        .stream()
                        .map(CommentResponse::from)
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
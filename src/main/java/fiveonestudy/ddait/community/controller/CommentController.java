package fiveonestudy.ddait.community.controller;

import fiveonestudy.ddait.community.dto.CommentResponse;
import fiveonestudy.ddait.community.service.CommentService;
import fiveonestudy.ddait.global.exception.UnauthorizedException;
import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody Map<String, Object> body
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        Long id = commentService.create(
                userDetails.getUser(),
                postId,
                (String) body.get("content"),
                body.get("parentId") == null ? null : Long.valueOf(body.get("parentId").toString())
        );

        return ResponseEntity.ok(ApiResponse.success(Map.of("commentId", id)));
    }

    // 댓글 조회
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

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        commentService.delete(userDetails.getUser(), commentId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
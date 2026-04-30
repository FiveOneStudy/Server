package fiveonestudy.ddait.community.controller;

import fiveonestudy.ddait.community.dto.CreatePostRequest;
import fiveonestudy.ddait.community.dto.PostResponse;
import fiveonestudy.ddait.community.entity.PostSort;
import fiveonestudy.ddait.community.service.PostService;
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
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreatePostRequest request
    ) {

        Long id = postService.create(
                userDetails.getUser(),
                request.title(),
                request.content()
        );

        return ResponseEntity.ok(ApiResponse.success(Map.of("postId", id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPosts(
            @RequestParam(defaultValue = "LATEST") PostSort sort
    ) {

        List<PostResponse> result = postService.getPosts(sort)
                .stream()
                .map(PostResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {

        return ResponseEntity.ok(
                ApiResponse.success(PostResponse.from(postService.getPost(postId)))
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException();
        }
        postService.delete(userDetails.getUser(), postId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> likePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        postService.like(userDetails.getUser(), postId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        postService.unlike(userDetails.getUser(), postId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

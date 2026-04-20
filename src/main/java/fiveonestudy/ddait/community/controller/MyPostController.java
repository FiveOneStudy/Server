package fiveonestudy.ddait.community.controller;

import fiveonestudy.ddait.community.dto.MyCommentResponse;
import fiveonestudy.ddait.community.dto.MyPostResponse;
import fiveonestudy.ddait.community.service.MyPostService;
import fiveonestudy.ddait.global.exception.UnauthorizedException;
import fiveonestudy.ddait.global.response.ApiResponse;
import fiveonestudy.ddait.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/my")
public class MyPostController {

    private final MyPostService myPostService;

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<MyPostResponse>>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        List<MyPostResponse> result =
                myPostService.getMyPosts(userDetails.getId())
                        .stream()
                        .map(MyPostResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<List<MyCommentResponse>>> getMyComments(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            throw new UnauthorizedException();
        }

        List<MyCommentResponse> result =
                myPostService.getMyComments(userDetails.getId())
                        .stream()
                        .map(MyCommentResponse::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

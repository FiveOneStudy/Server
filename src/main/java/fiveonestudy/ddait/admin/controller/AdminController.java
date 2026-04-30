package fiveonestudy.ddait.admin.controller;

import fiveonestudy.ddait.admin.dto.CommentAdminResponse;
import fiveonestudy.ddait.admin.dto.PostAdminResponse;
import fiveonestudy.ddait.admin.service.AdminService;
import fiveonestudy.ddait.community.entity.Comment;
import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/posts/pending")
    public ApiResponse<List<PostAdminResponse>> pendingPosts() {
        return ApiResponse.success(adminService.getPendingPosts());
    }

    @GetMapping("/comments/pending")
    public ApiResponse<List<CommentAdminResponse>> pendingComments() {
        return ApiResponse.success(adminService.getPendingComments());
    }

    @PostMapping("/posts/{id}/approve")
    public void approvePost(@PathVariable Long id) {
        adminService.approvePost(id);
    }

    @PostMapping("/posts/{id}/reject")
    public void rejectPost(@PathVariable Long id) {
        adminService.rejectPost(id);
    }

    @PostMapping("/comments/{id}/approve")
    public void approveComment(@PathVariable Long id) {
        adminService.approveComment(id);
    }

    @PostMapping("/comments/{id}/reject")
    public void rejectComment(@PathVariable Long id) {
        adminService.rejectComment(id);
    }
}
package com.example.commentservice.controller;


import com.example.commentservice.dto.*;
import com.example.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{topicId}")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(@PathVariable("topicId") String topicId, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();

        CommentResponse commentResponse = commentService.saveComment(commentRequest, userId, topicId);
        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .status("success")
                .message("Comment created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{topicId}/comments")
    public ResponseEntity<ApiResponse<CommentsResponse>> getComments(
                            @PathVariable("topicId") String topicId,
                            @RequestParam(required = false)Instant cursor,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable  pageable =  PageRequest.of(0, size);
        ApiResponse<CommentsResponse> response = commentService.getComments(topicId, pageable, cursor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/comment/{comment-id}/reply")
    public ResponseEntity<RepliedComment> repliedComment(
                            @PathVariable("comment-id") String commentId,
                            @RequestBody CommentRequest commentRequest,
                            Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        RepliedComment repliedComment = commentService.repliedCommentSave(userId, commentId, commentRequest);
        return new ResponseEntity<>(repliedComment, HttpStatus.CREATED);
    }

    @PutMapping("/comment/{comment-id}/upvote")
    public ResponseEntity<UpvoteResponse> upvoteComment(@PathVariable("comment-id") String commentId, Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        UpvoteResponse upvoteResponse = commentService.upvoteComment(userId, commentId);
        if (Objects.equals(upvoteResponse.getStatus(), "failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(upvoteResponse);
        }
        return ResponseEntity.ok(upvoteResponse);
    }

    @PutMapping("/comment/{comment-id}/downvote")
    public ResponseEntity<DownvoteResponse> downvoteComment(@PathVariable("comment-id") String commentId, Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        DownvoteResponse downvoteResponse = commentService.downvoteComment(userId, commentId);
        if (Objects.equals(downvoteResponse.getStatus(), "failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(downvoteResponse);
        }
        return ResponseEntity.ok(downvoteResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/comment/{comment-id}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable("comment-id") String commentId) {
        DeleteCommentResponse deleteCommentResponse = commentService.deleteComment(commentId);
        if (Objects.equals(deleteCommentResponse.getStatus(), "failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteCommentResponse);
        }
        return ResponseEntity.ok(deleteCommentResponse);
    }
}


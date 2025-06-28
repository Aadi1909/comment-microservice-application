package com.example.commentservice.controller;


import com.example.commentservice.dto.ApiResponse;
import com.example.commentservice.dto.CommentRequest;
import com.example.commentservice.dto.CommentResponse;
import com.example.commentservice.dto.CommentsResponse;
import com.example.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.time.Instant;

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
                            @RequestBody CommentRequest commentRequest) {
        RepliedComment repliedComment = commentService.repliedCommentSave(commentId, commentRequest);
        return new ResponseEntity<>(repliedComment, HttpStatus.CREATED);
    }

}


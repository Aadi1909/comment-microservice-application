package com.example.commentservice.service;


import com.example.commentservice.dao.CommentRepository;
import com.example.commentservice.dao.UserClient;
import com.example.commentservice.dto.*;
import com.example.commentservice.model.Comments;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserClient userClient;

    public CommentResponse saveComment(CommentRequest commentRequest, Integer userId, String topicId) {
        UserDto user = userClient.getUserById(userId);
        Comments comment = Comments
                .builder()
                .content(commentRequest.getContent())
                .topicId(topicId)
                .userId(userId)
                .timestamp(Instant.now())
                .build();
        String commentId = commentRepository.save(comment).getId();
        return CommentResponse
                .builder()
                .id(commentId)
                .content(comment.getContent())
                .posterId(userId)
                .posterName(user.getUsername())
                .postTime(comment.getTimestamp())
                .build();
    }

    public ApiResponse<CommentsResponse> getComments(String topicId, Pageable pageable, Instant cursor) {
        List<Comments> comments;
        if (cursor != null) {
            comments = commentRepository.findByTopicIdAndTimestampLessThanOrderByTimestampDesc(topicId, cursor, pageable);
        } else {
            comments = commentRepository.findByTopicIdOrderByTimestampDesc(topicId, pageable);
        }

        // check the size of the comment list to show the latest comment for fetching
        if (comments.isEmpty()) {
            return ApiResponse.<CommentsResponse>builder()
                    .status("success")
                    .message("No comments found")
                    .data(CommentsResponse.builder()
                            .total(0L)
                            .comments(List.of())
                            .build())
                    .build();
        }
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> CommentDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .posterId(comment.getUserId())
                        .posterName(userClient.getUserById(comment.getUserId()).getUsername())
                        .postTime(comment.getTimestamp())
                        .upvotesCount(comment.getUpvoteCount())
                        .downvotesCount(comment.getDownvoteCount())
                        .repliedCommentId(comment.getRepliedCommentId())
                        .build())
                .toList();
        Long total = commentRepository.countByTopicId(topicId);

        CommentsResponse commentsResponse = CommentsResponse.builder()
                .total(total)
                .comments(commentDtos)
                .build();
        return ApiResponse.<CommentsResponse>builder()
                .status("success")
                .message("Comments fetched successfully")
                .data(commentsResponse)
                .build();

    }


}

package com.example.commentservice.service;


import com.example.commentservice.dao.*;
import com.example.commentservice.dto.*;
import com.example.commentservice.model.Comments;
import com.example.commentservice.model.Downvotes;
import com.example.commentservice.model.Topics;
import com.example.commentservice.model.Upvotes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final TopicRepository topicRepository;

    private final CommentRepository commentRepository;

    private final UpvotesRepository upvotesRepository;

    private final DownvotesRepository downvotesRepository;

    private final UserClient userClient;

    public CommentResponse saveComment(CommentRequest commentRequest, Integer userId, String topicId) {
        // 1. Get user info from external user service
        UserDto user = userClient.getUserById(userId);

        // 2. Create and save the comment
        Comments comment = Comments.builder()
                .content(commentRequest.getContent())
                .topicId(topicId)
                .userId(userId)
                .timestamp(Instant.now())
                .build();

        // 3. Save comment
        Comments savedComment = commentRepository.save(comment);

        // 4. Ensure topic exists and increment totalCount
        Topics topic = topicRepository.findByTopicId(topicId)
                .orElse(Topics.builder()
                        .topicId(topicId)
                        .totalCount(0)
                        .build());

        topic.setTotalCount(topic.getTotalCount() + 1);
        topicRepository.save(topic);

        // 5. Return response
        return CommentResponse.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .posterId(userId)
                .posterName(user.getUsername())
                .postTime(savedComment.getTimestamp())
                .build();
    }


    public ApiResponse<CommentsResponse> getComments(String topicId, Pageable pageable, Instant cursor) {
        List<Comments> comments;
        if (cursor != null) {
            comments = commentRepository.findByTopicIdAndTimestampLessThanOrderByTimestampDesc(topicId, cursor, pageable);
        } else {
            comments = commentRepository.findByTopicIdOrderByTimestampDesc(topicId, pageable);
        }

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

    public RepliedComment repliedCommentSave(Integer userId, String commentId, CommentRequest commentRequest) {
        String userName = userClient.getUserById(userId).getUsername();
        Comments parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        Comments comment = Comments
                .builder()
                .content(commentRequest.getContent())
                .topicId(commentId)
                .userId(userId)
                .timestamp(Instant.now())
                .build();
        String id = commentRepository.save(comment).getId();
        RepliedCommentDto repliedCommentDto = RepliedCommentDto
                .builder()
                .id(id)
                .content(comment.getContent())
                .replierId(userId)
                .replierName(userName)
                .replyTime(comment.getTimestamp())
                .repliedCommentId(parentComment.getId())
                .build();

        return RepliedComment
                .builder()
                .status("success")
                .message("Replied comment saved successfully")
                .comment(repliedCommentDto)
                .build();
    }

    public UpvoteResponse upvoteComment(Integer userId, String commentId) {
        Optional<Upvotes> existing = upvotesRepository.findByCommentIdAndUserId(commentId, userId);
        if (existing.isPresent()) {
            return UpvoteResponse.builder()
                    .status("failed")
                    .message("You have already upvoted this comment")
                    .build();
        }

        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        String topicId = comment.getTopicId();
        Upvotes upvotes = Upvotes
                .builder()
                .topicId(topicId)
                .commentId(commentId)
                .userId(userId)
                .timestamp(Instant.now())
                .build();
        upvotesRepository.save(upvotes);
        return UpvoteResponse
                .builder()
                .status("success")
                .message("Upvoted comment successfully")
                .build();
    }


    public DownvoteResponse downvoteComment(Integer userId, String commentId) {
        boolean hasUserDownvote = downvotesRepository.findByCommentIdAndUserId(commentId, userId).isPresent();

        String topicId = downvotesRepository.findByCommentIdAndUserId(commentId, userId).get().getTopicId();
        if (hasUserDownvote) {
            return DownvoteResponse
                    .builder()
                    .status("failed")
                    .message("You have already upvoted this comment")
                    .build();
        }
        Downvotes downvotes = Downvotes
                .builder()
                .topicId(topicId)
                .commentId(commentId)
                .userId(userId)
                .timestamp(Instant.now())
                .build();
        downvotesRepository.save(downvotes);
        return DownvoteResponse
                .builder()
                .status("success")
                .message("Upvoted comment successfully")
                .build();
    }

    public DeleteCommentResponse deleteComment(String commentId) {
        // 1. Check if root comment exists
        Optional<Comments> rootOptional = commentRepository.findById(commentId);
        if (rootOptional.isEmpty()) {
            return DeleteCommentResponse.builder()
                    .status("failed")
                    .message("Comment not found")
                    .build();
        }
        Comments root = rootOptional.get();
        String topicId = root.getTopicId();

        // 2. Get all related comment IDs (the root + replies)
        List<String> allCommentIds = findAllDescendantCommentIds(commentId);
        allCommentIds.add(commentId); // include the root comment itself

        // 3. Delete upvotes and downvotes
        upvotesRepository.deleteByCommentIdIn(allCommentIds);
        downvotesRepository.deleteByCommentIdIn(allCommentIds);

        // 4. Delete all comments (root + descendants)
        commentRepository.deleteAllById(allCommentIds);

        // 5. Decrement the comment count in topic
        Topics topic = topicRepository.findByTopicId(topicId).orElse(null);
        if (topic != null) {
            int newCount = Math.max(0, topic.getTotalCount() - allCommentIds.size());
            topic.setTotalCount(newCount);
            topicRepository.save(topic);
        }

        return DeleteCommentResponse.builder()
                .status("success")
                .message("Comment and all its replies deleted successfully")
                .build();
    }

    private List<String> findAllDescendantCommentIds(String parentId) {
        List<String> descendants = new ArrayList<>();
        List<Comments> children = commentRepository.findByRepliedCommentId(parentId);
        for (Comments child : children) {
            descendants.add(child.getId());
            descendants.addAll(findAllDescendantCommentIds(child.getId()));
        }
        return descendants;
    }

}

package com.example.commentservice.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private String id;
    private String content;
    private Integer posterId;
    private String posterName;
    private Instant postTime;
    private Integer upvotesCount;
    private Integer downvotesCount;
    private String repliedCommentId;
}

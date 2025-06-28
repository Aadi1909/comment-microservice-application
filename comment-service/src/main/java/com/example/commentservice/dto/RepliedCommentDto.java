package com.example.commentservice.dto;

import lombok.*;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepliedCommentDto {

    private String id;
    private String content;
    private Integer replierId;
    private String replierName;
    private Instant replyTime;
    private String repliedCommentId;
}

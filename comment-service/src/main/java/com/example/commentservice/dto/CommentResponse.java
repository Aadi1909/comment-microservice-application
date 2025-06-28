package com.example.commentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {

    private String id;
    private String content;
    private Integer posterId;
    private String posterName;
    private Instant postTime;

}

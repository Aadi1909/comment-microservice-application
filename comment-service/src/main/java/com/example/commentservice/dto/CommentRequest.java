package com.example.commentservice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class CommentRequest {

    private String content;
}

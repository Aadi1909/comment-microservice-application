package com.example.commentservice.dto;


import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepliedComment {

    private String status;
    private String message;
    private RepliedCommentDto comment;

}

package com.example.commentservice.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteCommentResponse {

    private String status;
    private String message;
}


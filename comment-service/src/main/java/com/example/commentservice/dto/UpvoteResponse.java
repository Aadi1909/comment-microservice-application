package com.example.commentservice.dto;


import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpvoteResponse {

    private String status;
    private String message;
}

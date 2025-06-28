package com.example.commentservice.dto;


import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsResponse {

    private Long total;
    private List<CommentDto> comments;
}

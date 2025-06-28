package com.example.commentservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class Comments {

    @Id
    private String id;

    private String content;

    @Indexed
    private String topicId;

    @Indexed
    private Integer userId;

    private String repliedCommentId;

    private Integer upvoteCount;
    private Integer downvoteCount;

    private Instant timestamp;

}

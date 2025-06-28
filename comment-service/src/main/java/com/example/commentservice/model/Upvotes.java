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
@Document(collection = "upvotes")
public class Upvotes {

    @Id
    private String id;

    private String topicId;

    @Indexed
    private String commentId;

    @Indexed
    private Integer userId;

    private Instant timestamp;
}

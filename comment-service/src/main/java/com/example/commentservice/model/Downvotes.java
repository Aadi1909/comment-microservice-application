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
@Document(collection = "downvotes")
public class Downvotes {

    @Id
    private String id;

    private String topicId;

    @Indexed
    private Integer userId;

    @Indexed
    private String commentId;

    private Instant timestamp;
}

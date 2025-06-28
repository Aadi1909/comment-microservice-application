package com.example.commentservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "topics")
public class Topics {
    @Id
    private String id;

    private String topicId;

    private String totalCount; // The total count of comments of the topic.
}

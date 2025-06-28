package com.example.commentservice.dao;

import com.example.commentservice.model.Comments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<Comments, String> {
    List<Comments> findByTopicId(String topicId);
    // Initial load: no cursor
    List<Comments> findByTopicIdOrderByTimestampDesc(String topicId, Pageable pageable);

    // Paginated load using cursor (timestamp)
    List<Comments> findByTopicIdAndTimestampLessThanOrderByTimestampDesc(String topicId, Instant cursor, Pageable pageable);

    long countByTopicId(String topicId);
}

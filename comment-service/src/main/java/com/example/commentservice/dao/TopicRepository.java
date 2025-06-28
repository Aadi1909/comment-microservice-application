package com.example.commentservice.dao;


import com.example.commentservice.model.Topics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TopicRepository extends MongoRepository<Topics, String> {

    Optional<Topics> findByTopicId(String topicId);
}

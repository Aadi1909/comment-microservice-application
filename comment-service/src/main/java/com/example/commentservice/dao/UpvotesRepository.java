package com.example.commentservice.dao;

import com.example.commentservice.model.Comments;
import com.example.commentservice.model.Upvotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpvotesRepository extends MongoRepository<Upvotes, String> {

    Optional<Upvotes> findByCommentIdAndUserId(String commentId, Integer userId);

    List<Upvotes> findByUserId(Integer userId);

    void deleteByCommentIdIn(List<String> commentIds);

    List<Comments> findByRepliedCommentId(String parentId);

}

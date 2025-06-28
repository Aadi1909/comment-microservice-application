package com.example.commentservice.dao;

import com.example.commentservice.model.Comments;
import com.example.commentservice.model.Downvotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DownvotesRepository extends MongoRepository<Downvotes, String> {

    Optional<Downvotes> findByCommentIdAndUserId(String commentId, Integer userId);

    List<Downvotes> findByUserId(Integer userId);
    void deleteByCommentIdIn(List<String> commentIds);

    List<Comments> findByRepliedCommentId(String parentId);

}
package com.pks.BloggingApplication.service;

import com.pks.BloggingApplication.model.Comment;
import com.pks.BloggingApplication.repository.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    ICommentRepo commentRepo;
    public String addComment(Comment comment) {
        commentRepo.save(comment);
        return "Comment  added..";
    }

    public Comment findComment(Integer commentId) {
        return  commentRepo.findById(commentId).orElse(null);
    }

    public void removeComment(Comment comment) {
        commentRepo.delete(comment);
    }
}

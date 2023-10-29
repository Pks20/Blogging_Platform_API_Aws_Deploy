package com.pks.BloggingApplication.repository;

import com.pks.BloggingApplication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepo extends JpaRepository<Comment,Integer> {
}

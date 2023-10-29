package com.pks.BloggingApplication.repository;

import com.pks.BloggingApplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepo extends JpaRepository<Post,Integer> {
}

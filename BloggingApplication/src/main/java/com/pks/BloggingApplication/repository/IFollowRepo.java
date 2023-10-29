package com.pks.BloggingApplication.repository;

import com.pks.BloggingApplication.model.Follow;
import com.pks.BloggingApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFollowRepo extends JpaRepository<Follow,Integer> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User targetUser, User follower);
}

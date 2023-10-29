package com.pks.BloggingApplication.repository;

import com.pks.BloggingApplication.model.AuthenticationToken;
import com.pks.BloggingApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthenticationRepo extends JpaRepository<AuthenticationToken,Long> {
    AuthenticationToken findFirstByTokenValue(String authTokenValue);
    AuthenticationToken findFirstByUser(User user);
}

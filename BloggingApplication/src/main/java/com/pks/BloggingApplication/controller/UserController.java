package com.pks.BloggingApplication.controller;

import com.pks.BloggingApplication.model.Comment;
import com.pks.BloggingApplication.model.Follow;
import com.pks.BloggingApplication.model.Post;
import com.pks.BloggingApplication.model.User;
import com.pks.BloggingApplication.model.dto.SignInInput;
import com.pks.BloggingApplication.model.dto.SignUpOutput;
import com.pks.BloggingApplication.service.AuthenticationService;
import com.pks.BloggingApplication.service.PostService;
import com.pks.BloggingApplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("user/signup")
    public SignUpOutput signUpBlogUser(@RequestBody @Valid User user)
    {
        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String sigInBlogUser(@RequestBody @Valid SignInInput signInInput)
    {
        return userService.sigInUser(signInInput);
    }

    @DeleteMapping("user/signOut")
    public String sigOutBlogUser(String email, String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.sigOutUser(email);
        }
        else {
            return "Sign out not allowed for  unauthenticated user.";
        }

    }

    @GetMapping("users")
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("follow")
    public String followUser(@RequestBody Follow follow, @RequestParam String followerEmail, @RequestParam String followerToken)
    {
        if(authenticationService.authenticate(followerEmail,followerToken)) {
            return userService.followUser(follow,followerEmail);
        }
        else {
            return "Not an Authenticated user ";
        }
    }

    @DeleteMapping("unfollow/{followId}")
    public String unFollowUser(@PathVariable Integer followId, @RequestParam String followerEmail, @RequestParam String followerToken)
    {
        if(authenticationService.authenticate(followerEmail,followerToken)) {
            return userService.unFollowUser(followId,followerEmail);
        }
        else {
            return "Not an Authenticated user";
        }
    }
    @PostMapping("post")
    public String createBlogPost(@RequestBody Post post, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.createBlogPost(post,email);
        }
        else {
            return "Not an Authenticated user";
        }
    }

    @DeleteMapping("post")
    public String removeBlogPost(@RequestParam Integer postId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeBlogPost(postId,email);
        }
        else {
            return "Not an Authenticated user";
        }
    }

    @PutMapping("user/post/{userId}")
    public Post updateBlogPost(@PathVariable Integer userId, @RequestBody Post updatedPost)
    {
        return userService.updateBlogPost(userId,updatedPost);
    }
    @GetMapping("posts")
    public List<Post> getAllPosts()
    {
        return postService.getAllPosts();
    }

    @PostMapping("comment")
    public String addComment(@RequestBody Comment comment, @RequestParam String commenterEmail, @RequestParam String commenterToken)
    {
        if(authenticationService.authenticate(commenterEmail,commenterToken)) {
            return userService.addComment(comment,commenterEmail);
        }
        else {
            return "Not an Authenticated user";
        }
    }


    @DeleteMapping("comment")
    public String removeBlogComment(@RequestParam Integer commentId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeBlogComment(commentId,email);
        }
        else {
            return "Not an Authenticated user";
        }
    }


    @GetMapping("post/{postId}")
    public Optional<Post> getPostById(@PathVariable Integer postId)
    {
        return postService.getPostById(postId);
    }

}

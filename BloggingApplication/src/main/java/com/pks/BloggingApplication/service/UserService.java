package com.pks.BloggingApplication.service;

import com.pks.BloggingApplication.model.*;
import com.pks.BloggingApplication.model.dto.SignInInput;
import com.pks.BloggingApplication.model.dto.SignUpOutput;
import com.pks.BloggingApplication.repository.IPostRepo;
import com.pks.BloggingApplication.repository.IUserRepo;
import com.pks.BloggingApplication.service.emailUtility.EmailHandler;
import com.pks.BloggingApplication.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    FollowService followService;

    @Autowired
    IPostRepo postRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public SignUpOutput signUpUser(User user) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = user.getUserEmail();

        if(newEmail == null)
        {
            signUpStatusMessage = "It is Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }


        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if(existingUser != null)
        {
            signUpStatusMessage = "Email already registered";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }


        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());



            user.setUserPassword(encryptedPassword);
            userRepo.save(user);

            return new SignUpOutput(signUpStatus, "User registered successfully");
        }
        catch(Exception e)
        {
            signUpStatusMessage = "Internal error  during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
    }

    public String sigInUser(SignInInput signInInput) {
        String signInStatusMessage = null;

        String signInEmail = signInInput.getEmail();

        if(signInEmail == null)
        {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }


        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if(existingUser == null)
        {
            signInStatusMessage = "Email not registered";
            return signInStatusMessage;

        }




        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if(existingUser.getUserPassword().equals(encryptedPassword))
            {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken  = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);

                EmailHandler.sendEmail(signInEmail,"email testing",authToken.getTokenValue());
                return "Token sent to your email";
            }
            else {
                signInStatusMessage = "Invalid credentials";
                return signInStatusMessage;
            }
        }
        catch(Exception e)
        {
            signInStatusMessage = "Internal error  during sign in";
            return signInStatusMessage;
        }

    }
    public String sigOutUser(String email) {
        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken token = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(token);
        return "User Signed out successfully";
    }

    public String createBlogPost(Post post, String email) {
        User postOwner = userRepo.findFirstByUserEmail(email);
        post.setPostOwner(postOwner);
        return postService.createBlogPost(post);
    }

    public String removeBlogPost(Integer postId, String email) {
        User user = userRepo.findFirstByUserEmail(email);
        return postService.removeBlogPost(postId,user);
    }

    public String addComment(Comment comment, String commenterEmail) {
        boolean postValid = postService.validatePost(comment.getBlogPost());
        if(postValid) {
            User commenter = userRepo.findFirstByUserEmail(commenterEmail);
            comment.setCommenter(commenter);
            return commentService.addComment(comment);
        }
        else {
            return "Cannot comment on Invalid Post";
        }
    }
    private boolean authorizeCommentRemover(String email, Comment comment) {
        String  commentOwnerEmail = comment.getCommenter().getUserEmail();
        String  postOwnerEmail  = comment.getBlogPost().getPostOwner().getUserEmail();

        return postOwnerEmail.equals(email) || commentOwnerEmail.equals(email);
    }
    public String removeBlogComment(Integer commentId, String email) {
        Comment comment  = commentService.findComment(commentId);
        if(comment!=null)
        {
            if(authorizeCommentRemover(email,comment))
            {
                commentService.removeComment(comment);
                return "comment deleted";
            }
            else
            {
                return "Unauthorized user...Not allowed";
            }

        }
        else
        {
            return "Invalid Comment";
        }
    }

    public String followUser(Follow follow, String followerEmail) {

        User followTargetUser = userRepo.findById(follow.getCurrentUser().getUserId()).orElse(null);

        User follower = userRepo.findFirstByUserEmail(followerEmail);

        if(followTargetUser!=null)
        {
            if(followService.isFollowAllowed(followTargetUser,follower))
            {
                followService.startFollowing(follow,follower);
                return follower.getUserName()  + " is now following " + followTargetUser.getUserName();
            }
            else {
                return follower.getUserName()  + " already follows " + followTargetUser.getUserName();
            }
        }
        else {
            return "User  is Invalid";
        }
    }

    public Post updateBlogPost(Integer userId, Post updatedPost) {

        Optional<Post> optionalPost = postRepo.findById(userId);
        if (!optionalPost.isPresent()) {

            throw new IllegalArgumentException("Post not found with ID: " + userId);
        }

        Post existingPost = optionalPost.get();

        existingPost.setPostCaption(updatedPost.getPostCaption());
        existingPost.setPostContent(updatedPost.getPostContent());
        existingPost.setPostType(updatedPost.getPostType());

        return postRepo.save(existingPost);

    }

    private boolean authorizeUnfollow(String email, Follow follow)
    {
        String  targetEmail = follow.getCurrentUser().getUserEmail();
        String  followerEmail  = follow.getCurrentUserFollower().getUserEmail();

        return targetEmail.equals(email) || followerEmail.equals(email);
    }
    public String unFollowUser(Integer followId, String followerEmail) {

        Follow follow  = followService.findFollow(followId);
        if(follow != null)
        {
            if(authorizeUnfollow(followerEmail,follow))
            {
                followService.unfollow(follow);
                return follow.getCurrentUser().getUserName() + " not followed by " + followerEmail;
            }
            else
            {
                return "Unauthorized unfollow Not allowed";
            }

        }
        else
        {
            return "Invalid follow ";
        }
    }

}

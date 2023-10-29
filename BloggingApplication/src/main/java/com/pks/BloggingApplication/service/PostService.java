package com.pks.BloggingApplication.service;

import com.pks.BloggingApplication.model.Post;
import com.pks.BloggingApplication.model.User;
import com.pks.BloggingApplication.repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    IPostRepo postRepo;

    public String createBlogPost(Post post) {
        postRepo.save(post);
        return "Post uploaded!!!!";
    }

    public String removeBlogPost(Integer postId, User user) {
        Post post  = postRepo.findById(postId).orElse(null);
        if(post != null && post.getPostOwner().equals(user))
        {
            postRepo.deleteById(postId);
            return "Removed ";
        }
        else if (post == null)
        {
            return "Post  does not exist";
        }
        else{
            return "Un-Authorized it is Not allowed";
        }
    }

    public boolean validatePost(Post blogPost) {
        return (blogPost!=null && postRepo.existsById(blogPost.getPostId()));
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    public Optional<Post> getPostById(Integer postId) {
        return postRepo.findById(postId);
    }
}

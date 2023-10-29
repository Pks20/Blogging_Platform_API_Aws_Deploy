package com.pks.BloggingApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(nullable = false)
    private String commentContent;


    @ManyToOne
    @JoinColumn(name = "fk_comment_post_id")
    private Post blogPost;

    @ManyToOne
    @JoinColumn(name = "fk_commenter_id")
    private User commenter;
}

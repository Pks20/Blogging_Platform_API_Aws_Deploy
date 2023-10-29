package com.pks.BloggingApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer PostId;
    private String postCaption;
    private String postContent;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "fk_post_user_id")
    private User postOwner;
}

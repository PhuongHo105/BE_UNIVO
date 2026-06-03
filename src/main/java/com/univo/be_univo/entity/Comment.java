package com.univo.be_univo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;
    @ManyToOne
    @JoinColumn (name = "postId")
    private Post post;
    private Date createdAt;
    private Date updatedAt;
    private boolean status;
}

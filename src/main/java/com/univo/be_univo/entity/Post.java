package com.univo.be_univo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table (name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "topicId")
    private Topic topic;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private List<String> images;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private int upvote;
    private int downvote;
    private byte status;
    private List<String> tags;
}

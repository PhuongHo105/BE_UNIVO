package com.univo.be_univo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "topic_follows")
@Getter
@Setter
public class TopicFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn (name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn (name = "topicId")
    private Topic topic;
    private Date createdAt;
    private boolean status;
}

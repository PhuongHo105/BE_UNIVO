package com.univo.be_univo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "follows")
@Getter
@Setter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Date createdAt;
    @ManyToOne
    @JoinColumn (name = "followingId")
    private User following;
    @ManyToOne
    @JoinColumn (name = "followerId")
    private User follower;
    private boolean status;
}

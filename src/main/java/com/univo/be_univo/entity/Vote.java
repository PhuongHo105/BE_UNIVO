package com.univo.be_univo.entity;

import com.univo.be_univo.enums.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "votes")
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @Enumerated(EnumType.STRING)
    private VoteType type;
    private String targetId;
    private boolean targetType;
    private boolean status;
}

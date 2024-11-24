package com.splitclone.model.common;

import com.splitclone.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "group_roles")
public class GroupRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    public enum Role {
        ADMIN,
        MODERATOR,
        MEMBER
    }

    // Helper methods
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isModerator() {
        return role == Role.MODERATOR || role == Role.ADMIN;
    }
}

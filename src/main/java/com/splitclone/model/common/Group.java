package com.splitclone.model.common;

import com.splitclone.model.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<GroupRole> memberRoles = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupType type = GroupType.PRIVATE;

    @Column(name = "invite_code")
    private String inviteCode;

    public enum GroupType {
        PRIVATE,
        PUBLIC,
        INVITE_ONLY
    }

    // Helper methods
    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
        memberRoles.removeIf(role -> role.getUser().equals(user));
    }

    public boolean isMember(User user) {
        return members.contains(user);
    }
}

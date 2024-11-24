package com.splitclone.repository.common;

import com.splitclone.model.User;
import com.splitclone.model.common.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatorOrderByCreatedAtDesc(User creator);
    
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m = :user ORDER BY g.createdAt DESC")
    List<Group> findGroupsByMember(@Param("user") User user);
    
    @Query("SELECT g FROM Group g WHERE g.type = 'PUBLIC' ORDER BY g.createdAt DESC")
    List<Group> findPublicGroups();
    
    Optional<Group> findByInviteCode(String inviteCode);
    
    @Query("SELECT COUNT(m) > 0 FROM Group g JOIN g.members m " +
           "WHERE g = :group AND m = :user")
    boolean isMember(@Param("group") Group group, @Param("user") User user);
    
    @Query("SELECT gr.role FROM GroupRole gr " +
           "WHERE gr.group = :group AND gr.user = :user")
    Optional<Group.GroupType> getUserRole(@Param("group") Group group, @Param("user") User user);
}

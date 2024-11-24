package com.splitclone.service.common;

import com.splitclone.dto.common.CreateGroupRequest;
import com.splitclone.dto.common.GroupDTO;
import com.splitclone.model.User;
import com.splitclone.model.common.Group;
import com.splitclone.model.common.GroupRole;
import com.splitclone.repository.UserRepository;
import com.splitclone.repository.common.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupDTO createGroup(CreateGroupRequest request, User creator) {
        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreator(creator);
        group.setCreatedAt(LocalDateTime.now());
        group.setType(request.getType());
        
        if (request.getType() == Group.GroupType.INVITE_ONLY) {
            group.setInviteCode(generateInviteCode());
        }

        // Add creator as admin
        group.addMember(creator);
        GroupRole adminRole = new GroupRole();
        adminRole.setGroup(group);
        adminRole.setUser(creator);
        adminRole.setRole(GroupRole.Role.ADMIN);
        group.getMemberRoles().add(adminRole);

        // Add initial members if any
        if (request.getMemberIds() != null) {
            List<User> members = userRepository.findAllById(request.getMemberIds());
            members.forEach(member -> {
                group.addMember(member);
                GroupRole memberRole = new GroupRole();
                memberRole.setGroup(group);
                memberRole.setUser(member);
                memberRole.setRole(GroupRole.Role.MEMBER);
                group.getMemberRoles().add(memberRole);
            });
        }

        groupRepository.save(group);
        return convertToDTO(group);
    }

    @Transactional
    public GroupDTO joinGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getType() == Group.GroupType.PRIVATE) {
            throw new RuntimeException("Cannot join private group");
        }

        if (group.isMember(user)) {
            throw new RuntimeException("Already a member of this group");
        }

        group.addMember(user);
        GroupRole memberRole = new GroupRole();
        memberRole.setGroup(group);
        memberRole.setUser(user);
        memberRole.setRole(GroupRole.Role.MEMBER);
        group.getMemberRoles().add(memberRole);

        groupRepository.save(group);
        return convertToDTO(group);
    }

    @Transactional
    public GroupDTO joinGroupByInvite(String inviteCode, User user) {
        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        if (group.isMember(user)) {
            throw new RuntimeException("Already a member of this group");
        }

        group.addMember(user);
        GroupRole memberRole = new GroupRole();
        memberRole.setGroup(group);
        memberRole.setUser(user);
        memberRole.setRole(GroupRole.Role.MEMBER);
        group.getMemberRoles().add(memberRole);

        groupRepository.save(group);
        return convertToDTO(group);
    }

    public List<GroupDTO> getUserGroups(User user) {
        return groupRepository.findGroupsByMember(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GroupDTO> getPublicGroups() {
        return groupRepository.findPublicGroups()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void leaveGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getCreator().equals(user)) {
            throw new RuntimeException("Creator cannot leave the group");
        }

        group.removeMember(user);
        groupRepository.save(group);
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setCreatorId(group.getCreator().getId());
        dto.setCreatorName(group.getCreator().getName());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setType(group.getType());
        dto.setInviteCode(group.getInviteCode());
        
        dto.setMembers(group.getMembers().stream()
                .map(member -> new GroupDTO.MemberDTO(
                    member.getId(),
                    member.getName(),
                    group.getMemberRoles().stream()
                        .filter(role -> role.getUser().equals(member))
                        .findFirst()
                        .map(GroupRole::getRole)
                        .orElse(GroupRole.Role.MEMBER)
                ))
                .collect(Collectors.toList()));
        
        return dto;
    }
}

package com.splitclone.controller.common;

import com.splitclone.dto.common.CreateGroupRequest;
import com.splitclone.dto.common.GroupDTO;
import com.splitclone.model.User;
import com.splitclone.service.common.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(
            @RequestPart("group") CreateGroupRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.createGroup(request, user));
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getUserGroups(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.getUserGroups(user));
    }

    @GetMapping("/public")
    public ResponseEntity<List<GroupDTO>> getPublicGroups() {
        return ResponseEntity.ok(groupService.getPublicGroups());
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupDTO> joinGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.joinGroup(groupId, user));
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<GroupDTO> joinGroupByInvite(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.joinGroupByInvite(inviteCode, user));
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal User user) {
        groupService.leaveGroup(groupId, user);
        return ResponseEntity.ok().build();
    }
}

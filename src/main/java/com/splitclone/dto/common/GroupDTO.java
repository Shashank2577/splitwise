package com.splitclone.dto.common;

import com.splitclone.model.common.Group;
import com.splitclone.model.common.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime createdAt;
    private Group.GroupType type;
    private String inviteCode;
    private List<MemberDTO> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO {
        private Long id;
        private String name;
        private GroupRole.Role role;
    }
}

package com.splitclone.dto.common;

import com.splitclone.model.common.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequest {
    @NotBlank(message = "Group name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Group type is required")
    private Group.GroupType type;
    
    private List<Long> memberIds;
}

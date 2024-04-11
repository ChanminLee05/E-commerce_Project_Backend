package com.project.nexushub.dto;

import com.project.nexushub.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private UUID user_id;
    private String username;
    private String email;
    private String password;
    private String phone_number;
    private LocalDate created;
    private Set<RoleType> roles;
}

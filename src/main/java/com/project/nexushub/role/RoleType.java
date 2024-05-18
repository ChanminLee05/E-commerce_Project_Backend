package com.project.nexushub.role;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.nexushub.role.Permission.*;

public enum RoleType {
    ADMIN(Set.of(ADMIN_READ, ADMIN_DELETE, USER_READ, USER_DELETE)),
    USER(Set.of(USER_READ, USER_DELETE));

    private final Set<Permission> permission;

    RoleType(Set<Permission> permission) {
        this.permission = permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return permission.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
    }
}

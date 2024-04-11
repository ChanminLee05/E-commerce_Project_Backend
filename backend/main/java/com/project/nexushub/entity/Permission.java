package com.project.nexushub.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_POST("admin:post"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    USER_READ("user:read"),
    USER_POST("user:post"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    private final String permission;
}

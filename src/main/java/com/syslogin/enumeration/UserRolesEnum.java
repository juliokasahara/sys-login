package com.syslogin.enumeration;

import lombok.Getter;

@Getter
public enum UserRolesEnum {
    ROLE_ADMIN(1L, "ROLE_ADMIN"),
    ROLE_USER(2L, "ROLE_USER");
    private final Long roleId;

    public UserRolesEnum getRoleName() {
        return this;
    }

    private final String roleName;

    UserRolesEnum(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

}
package com.bookstore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    CUSTOMER("customer"),
    ADMIN("admin");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @JsonValue
    public String getRoleName() {
        return roleName;
    }

    @JsonCreator
    public static Role fromRoleName(String roleName) {
        for (Role role : Role.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + roleName);
    }
}

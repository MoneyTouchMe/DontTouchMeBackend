package com.example.donttouchme.common.oauth2.enums;

public enum Role {
    ADMIN,
    USER;

    public static Role stringToRole(String role) {
        if (role.equals(Role.USER.toString())) {
            return Role.USER;
        } else {
            return Role.ADMIN;
        }
    }
}

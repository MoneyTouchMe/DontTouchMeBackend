package com.example.donttouchme.common.oauth2.dto;


import com.example.donttouchme.common.oauth2.enums.Role;

public record UserDto(Role role,
                      String name,
                      String username) {
}

package com.example.donttouchme.common.oauth2.dto;


import com.example.donttouchme.common.oauth2.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {
    private Role role;
    private String name;
    private String username;

    public UserDto(Role role, String username) {
        this.role = role;
        this.username = username;
    }
}

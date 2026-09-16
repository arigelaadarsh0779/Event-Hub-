package com.project.Event_Hub.Auth.Dto;

import com.project.Event_Hub.Auth.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

    private String username;
    private String password;
    private String phone;
    private String email;
    private Role role;
}
package com.project.Event_Hub.Auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String password;
}

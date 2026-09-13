package com.project.Event_Hub.Auth.Dto;

import com.project.Event_Hub.Auth.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@Data@NoArgsConstructor@Builder
public class RegisterRequestDto {
    private String Username;
    private String Password;
    private String Phone;
    private Role role;

}

package com.project.Event_Hub.Auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
@Builder

public class LoginRequestDto {
    private String Username;
    private String Password;
}

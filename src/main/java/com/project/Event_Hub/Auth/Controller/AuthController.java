package com.project.Event_Hub.Auth.Controller;

import com.project.Event_Hub.Auth.Dto.AuthResponseDto;
import com.project.Event_Hub.Auth.Dto.LoginRequestDto;
import com.project.Event_Hub.Auth.Dto.RegisterRequestDto;
import com.project.Event_Hub.Auth.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService service;

  @PostMapping("/register")
    public ResponseEntity<String > register (RegisterRequestDto dto){
      service.register(dto);
      return ResponseEntity
              .status(200)
              .body("User Registered Succesfully");
  }

  @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login (LoginRequestDto dto){
      return ResponseEntity
              .status(200)
              .body(service.login(dto));
  }


}

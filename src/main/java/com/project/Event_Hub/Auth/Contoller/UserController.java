package com.project.Event_Hub.Auth.Contoller;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.format.SignStyle;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String greet(){
        return "Greet";
    }
    @PostMapping("/add")
    public String addStudent(@RequestBody User user){
        userService.createUser(user);
        return "Sucess";
    }

}

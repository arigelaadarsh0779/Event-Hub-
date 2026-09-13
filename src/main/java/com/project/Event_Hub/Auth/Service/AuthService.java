package com.project.Event_Hub.Auth.Service;

import com.project.Event_Hub.Auth.Dto.AuthResponseDto;
import com.project.Event_Hub.Auth.Dto.LoginRequestDto;
import com.project.Event_Hub.Auth.Dto.RegisterRequestDto;
import com.project.Event_Hub.Auth.Entity.Role;
import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Repository.UserRepository;
import com.project.Event_Hub.Auth.Security.JwtService;
import com.project.Event_Hub.Exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService service;

    private final PasswordEncoder passwordEncoder;


    public void register (RegisterRequestDto request){
        User user = new User();

        if (!repository.existsByUsername(request.getUsername()))
        {
            user.setUsername(request.getUsername());
            String password=request.getPassword();

            if (password!= null) {
                user.setPassword(passwordEncoder.encode(password));
            }
            else throw new PasswordNotNullException("Password must be not null ");
            Role role=request.getRole();

            if(!role.equals("USER")|| !role.equals("ADMIN")){
                throw new RoleIsNullException(role+":"+"is not a Role");
            }
            if (role.equals("USER") ||role.equals("ADMIN")){
                user.setRole(request.getRole());
            }
            else throw new InvalidRoleException("You Selected an Invalid Role");
            user.setPhone(request.getPhone());

            repository.save(user);

        }
        else throw new UserAlreadyExistException("This Username is Taken by Someone Please try another username");

    }




    public AuthResponseDto login(LoginRequestDto request){
        if (request.getPassword()==null){
            throw new PasswordNotNullException("Password must be not null");
        }
        if (request.getUsername()==null){
            throw new UserNameNotNullException("username must be not null");
        }

        User user = repository.findByUsername(request.getUsername());
        if(user.equals(null)){
            throw new RuntimeException("Invalid Username ");
        }

        boolean match= passwordEncoder.matches(request.getPassword(),user.getPassword());
        if(!match){
            throw new InvalidCredentials("Wrong password or username TryAgain");
        }

        String token = service.generateToken( user.getUsername());

        return  AuthResponseDto.builder()
                .token(token)
                .build();


    }
}

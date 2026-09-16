package com.project.Event_Hub.Auth.Service;

import com.project.Event_Hub.Auth.Dto.AuthResponseDto;
import com.project.Event_Hub.Auth.Dto.LoginRequestDto;
import com.project.Event_Hub.Auth.Dto.RegisterRequestDto;
import com.project.Event_Hub.Auth.Entity.Role;
import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Repository.UserRepository;
import com.project.Event_Hub.Auth.Security.JwtService;
import com.project.Event_Hub.Exception.*;
import com.project.Event_Hub.Notification.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService service;

    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;


    public void register(RegisterRequestDto request) {

        if (repository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistException(
                    "This Username is Taken by Someone Please try another username"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());

        if (request.getPassword()==null) {
            throw new PasswordNotNullException("Password must be not null");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() == null) {
            throw new RoleIsNullException("Role must not be null");
        }

        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());


        System.out.println("Username: " + request.getUsername());
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());
        System.out.println("Phone: " + request.getPhone());
        System.out.println("Role: " + request.getRole());


        repository.save(user);


//           emailSender.sendEmail(
//                    user.getEmail(),
//                    "Welcome to Event Hub 🎉",
//                    "Hello " + user.getUsername() + ",\n\n" +
//                            "Welcome to Event Hub! 🎉\n\n" +
//                            "Your account has been successfully created.\n\n" +
//                            "You can now log in to your Event Hub account and explore upcoming events, " +
//                            "book tickets, and manage your bookings.\n\n" +
//                            "Thank you for joining Event Hub!\n\n" +
//                            "Best regards,\n" +
//                            "Event Hub Team"
//            );



    }


    public AuthResponseDto login(LoginRequestDto request) {

        if (request.getPassword() == null) {
            throw new PasswordNotNullException("Password must be not null");
        }

        if (request.getUsername() == null) {
            throw new UserNameNotNullException("Username must be not null");
        }

        User user = repository.findByUsername(request.getUsername());

        if (user == null) {
            throw new RuntimeException("Invalid Username");
        }

        boolean match = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!match) {
            throw new InvalidCredentials("Wrong password or username. Try again");
        }

        String token = service.generateToken(user.getUsername());

        return AuthResponseDto.builder()
                .token(token)
                .userId(user.getUserid())
                .username(user.getUsername())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }
}

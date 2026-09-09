package com.project.Event_Hub.Auth.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Repository.UserRepository;
import com.project.Event_Hub.Notification.Service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final EmailSender sendEmail;


    public String createUser(User user){
        repository.save(user);

        sendEmail.sendEmail(
                user.getEmail(),
                "Welcome to Event Hub 🎉",
                "Hello " + user.getName() + ",\n\n" +
                        "Welcome to Event Hub! 🎉\n\n" +
                        "Your account has been successfully created.\n\n" +
                        "You can now log in to your Event Hub account and explore upcoming events, " +
                        "book tickets, and manage your bookings.\n\n" +
                        "Thank you for joining Event Hub!\n\n" +
                        "Best regards,\n" +
                        "Event Hub Team"
        );

        return "User Created SUCCESS";
    }

}

package com.project.Event_Hub.Auth.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public String createUser(User user){
        repository.save(user);
        return "User Created SUCCESS";
    }

}

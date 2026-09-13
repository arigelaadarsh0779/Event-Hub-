package com.project.Event_Hub.Auth.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    private String Username;

    @Column(unique = true, nullable = false)
    private String Email;

    private String Password;

    private String Phone;

    @Enumerated(EnumType.STRING)
    private Role role;
}

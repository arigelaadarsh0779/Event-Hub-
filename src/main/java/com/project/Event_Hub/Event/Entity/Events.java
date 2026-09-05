package com.project.Event_Hub.Event.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@Table(name = "Events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(nullable = false,length = 200 )
    private String Title;
    @Column(length = 400 )
    private String Description;
    @Column(nullable = false )
    private LocalDate Date;

    private LocalTime StartTime;

    private LocalTime EndTime;
    @Column(nullable = false,length = 200 )
    private String Venue;

    public String Organizer;



    private int Capacity ;






}

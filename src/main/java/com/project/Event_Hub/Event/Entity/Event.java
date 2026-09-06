package com.project.Event_Hub.Event.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "Event")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
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

    private String ThemeOfTheProject;

    public String Organizer;
    private int TotalSeats ;


    private int AvailableSeats;







}

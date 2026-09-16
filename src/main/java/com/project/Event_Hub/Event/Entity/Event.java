package com.project.Event_Hub.Event.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
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
    private long eventId;

    @Column(nullable = false,length = 200 )
    @NotBlank(message = " The Title name is Mandatory")
    private String title;

    @Column(length = 500 )
    private String Description;
    @Column(nullable = false )
    private LocalDate Date;

    private LocalTime StartTime;

    private LocalTime EndTime;
    @NotBlank(message = "Venue is Mandatory For your event..!")
    @Column(nullable = false,length = 200 )
    private String venue;

    private String themeOfTheProject;

    public String Organizer;
    @Min(10)
    private int TotalSeats ;
    @Positive
    private BigDecimal ticketPrice;

    @Positive
    private int AvailableSeats;







}

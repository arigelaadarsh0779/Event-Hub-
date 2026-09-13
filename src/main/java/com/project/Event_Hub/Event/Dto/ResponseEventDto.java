package com.project.Event_Hub.Event.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEventDto {
    private long eventid;
    private String Title;

    private String Description;

    private LocalDate Date;

    private LocalTime StartTime;

    private LocalTime EndTime;

    private String Venue;

    private String ThemeOfTheProject;

    private String Organizer;

    private BigDecimal ticketPrice;

    private int TotalSeats;
    private int RemainingSeats;

}

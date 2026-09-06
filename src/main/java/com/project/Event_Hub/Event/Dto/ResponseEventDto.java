package com.project.Event_Hub.Event.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEventDto {
    private String Title;

    private String Description;

    private LocalDate Date;

    private LocalTime StartTime;

    private LocalTime EndTime;

    private String Venue;

    private String ThemeOfTheProject;

    public String Organizer;

    private int TotalSeats ;
    private int RemainingSeats;

}

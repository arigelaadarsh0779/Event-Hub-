package com.project.Event_Hub.Event.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEventDto {

        private String Title;

        private String Description;

        private LocalDate Date;

        private LocalTime StartTime;

        private LocalTime EndTime;

        private String Venue;

        private String ThemeOfTheProject;

        private String Organizer;
    }


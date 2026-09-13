package com.project.Event_Hub.Exception.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponseDto {

        private LocalDateTime timestamp;
        private int status;
        private String errorMessage;


}

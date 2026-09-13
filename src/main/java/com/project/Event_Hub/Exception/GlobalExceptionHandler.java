package com.project.Event_Hub.Exception;

import com.project.Event_Hub.Exception.Dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DateExpiredExeception.class)
    public ResponseEntity<ErrorResponseDto> dateExpiredhandler(DateExpiredExeception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(LocalDateTime.now(),404,e.getMessage()));
    }

    @ExceptionHandler(NoEventFoundException.class)
    public ResponseEntity<ErrorResponseDto> NoEventFoundhandler(NoEventFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(LocalDateTime.now(),404,e.getMessage()));
    }

    @ExceptionHandler(TitleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TitleNotFoundHadler(TitleNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(LocalDateTime.now(),404,e.getMessage()));
    }
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TitleNotFoundHadler(EventNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(LocalDateTime.now(),404,e.getMessage()));
    }
    @ExceptionHandler(BookingsNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TitleNotFoundHadler(BookingsNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(LocalDateTime.now(),404,e.getMessage()));
    }



}

package com.project.Event_Hub.Exception;

import com.project.Event_Hub.Exception.Dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateExpiredExeception.class)
    public ResponseEntity<ErrorResponseDto> dateExpiredhandler(DateExpiredExeception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage()));
    }

    @ExceptionHandler(NoEventFoundException.class)
    public ResponseEntity<ErrorResponseDto> NoEventFoundhandler(NoEventFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(LocalDateTime.now(), 404, e.getMessage()));
    }

    @ExceptionHandler(TitleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TitleNotFoundHadler(TitleNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(LocalDateTime.now(), 404, e.getMessage()));
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> EventNotFoundException(EventNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(LocalDateTime.now(), 404, e.getMessage()));
    }

    @ExceptionHandler(BookingsNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> BookingsNotFoundException(BookingsNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(LocalDateTime.now(), 404, e.getMessage()));
    }

    @ExceptionHandler(PasswordNotNullException.class)
    public ResponseEntity<ErrorResponseDto> PasswordNotNullException(PasswordNotNullException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage()));
    }

    @ExceptionHandler(RoleIsNullException.class)
    public ResponseEntity<ErrorResponseDto> RoleIsNullException(RoleIsNullException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponseDto> InvalidRoleException(InvalidRoleException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> UserAlreadyExistException(UserAlreadyExistException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(LocalDateTime.now(), 409, e.getMessage()));
    }

    @ExceptionHandler(UserNameNotNullException.class)
    public ResponseEntity<ErrorResponseDto> UserNameNotNullException(UserNameNotNullException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<ErrorResponseDto> InvalidCredentials(InvalidCredentials e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(LocalDateTime.now(), 401, e.getMessage()));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException e){
        String msg = "Data conflict occurred";
        if (e.getMessage() != null && e.getMessage().contains("username")) {
            msg = "This username is already taken. Please try another username.";
        } else if (e.getRootCause() != null && e.getRootCause().getMessage() != null) {
            msg = e.getRootCause().getMessage();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(LocalDateTime.now(), 409, msg));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(LocalDateTime.now(), 400, e.getMessage() != null ? e.getMessage() : "Request failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(LocalDateTime.now(), 500, e.getMessage() != null ? e.getMessage() : "An unexpected server error occurred"));
    }
}


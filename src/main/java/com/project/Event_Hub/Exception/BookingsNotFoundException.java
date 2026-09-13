package com.project.Event_Hub.Exception;

public class BookingsNotFoundException extends RuntimeException {
    public BookingsNotFoundException(String message) {
        super(message);
    }
}

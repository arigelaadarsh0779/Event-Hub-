package com.project.Event_Hub.Exception;

public class NoEventFoundException extends RuntimeException {
    public NoEventFoundException(String message) {
        super(message);
    }
}

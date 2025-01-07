package com.example.workflowapi.exceptions;

public class AlreadyLikedException extends IllegalArgumentException {
    public AlreadyLikedException(String message) {
        super(message);
    }
}

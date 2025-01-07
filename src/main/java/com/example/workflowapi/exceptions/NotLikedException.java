package com.example.workflowapi.exceptions;

public class NotLikedException extends IllegalStateException {
    public NotLikedException(String message) {
        super(message);
    }
}

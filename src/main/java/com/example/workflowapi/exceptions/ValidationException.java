package com.example.workflowapi.exceptions;

import java.util.List;

public class ValidationException extends Throwable{
    private final List<String> errors;
    public ValidationException(List<String> errors){
        this.errors = errors;
    }
}

package com.example.workflowapi.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends Throwable {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }
}

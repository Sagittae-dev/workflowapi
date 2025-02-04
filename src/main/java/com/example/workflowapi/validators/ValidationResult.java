package com.example.workflowapi.validators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {
    private boolean isValid;
    private List<String> errors;

    public ValidationResult() {

    }
}

package com.example.workflowapi.validators;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationResult {
    private boolean isValid;
    private List<String> errors;
}

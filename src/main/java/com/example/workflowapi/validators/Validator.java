package com.example.workflowapi.validators;

public interface Validator<T> {
    ValidationResult validate(T entity);
}

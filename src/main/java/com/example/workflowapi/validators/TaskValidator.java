package com.example.workflowapi.validators;

import com.example.workflowapi.model.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskValidator implements Validator<Task> {
    @Override
    public ValidationResult validate(Task task) {
        List<String> errors = new ArrayList<>();

        if (task.getName() == null || task.getName().isEmpty()) {
            errors.add("Task name couldn't be empty.");
        }
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            errors.add("Due date cannot be in the past");
        }
        validateEnumField(task.getTaskStatus(), "task status", errors);
        validateEnumField(task.getTaskType(), "task type", errors);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(errors.isEmpty());
        validationResult.setErrors(errors);
        return validationResult;
    }

    private void validateEnumField(Enum<?> fieldValue, String fieldName, List<String> errors) {
        if (fieldValue != null) {
            try {
                Enum.valueOf(fieldValue.getDeclaringClass(), fieldValue.toString());
            } catch (IllegalArgumentException e) {
                errors.add("Invalid " + fieldName + ": " + fieldValue);
            }
        }
    }
}

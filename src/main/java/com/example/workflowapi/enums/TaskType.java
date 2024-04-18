package com.example.workflowapi.enums;

import java.util.Optional;

public enum TaskType {
    TECHNICAL_TASK, BUG, STORY;

    public static Optional<TaskType> fromString(String value) {
        try {
            return Optional.of(TaskType.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
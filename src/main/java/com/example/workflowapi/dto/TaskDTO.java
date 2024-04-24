package com.example.workflowapi.dto;

import com.example.workflowapi.enums.TaskPriority;
import com.example.workflowapi.enums.TaskStatus;
import com.example.workflowapi.enums.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String name;
    private TaskPriority priority;
    private LocalDateTime creationDate;
    private TaskType taskType;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private String createdBy;
    private String assignedTo;
    private List<CommentDTO> comments;
    private String attachment;
}

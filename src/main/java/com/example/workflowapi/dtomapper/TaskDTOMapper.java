package com.example.workflowapi.dtomapper;

import com.example.workflowapi.dto.TaskDTO;
import com.example.workflowapi.model.Task;

import java.util.stream.Collectors;

@Deprecated
public class TaskDTOMapper {
    public static TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setPriority(task.getPriority());
        dto.setCreationDate(task.getCreationDate());
        dto.setTaskType(task.getTaskType());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setTaskStatus(task.getTaskStatus());
        dto.setCreatedBy(task.getCreatedBy() != null ? task.getCreatedBy().getUsername() : null);
        dto.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null);
        dto.setComments(task.getComments() != null ? task.getComments().stream().map(CommentDTOMapper::mapToDTO).collect(Collectors.toList()) : null);
        dto.setAttachment(task.getAttachment());
        return dto;
    }
}

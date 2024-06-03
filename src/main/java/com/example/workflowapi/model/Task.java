package com.example.workflowapi.model;

import com.example.workflowapi.enums.TaskPriority;
import com.example.workflowapi.enums.TaskStatus;
import com.example.workflowapi.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private TaskPriority priority;
    @CreatedDate
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    @ManyToOne
    private WorkflowUser createdBy;
    @ManyToOne
    private WorkflowUser assignedTo;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;
    private String attachment;
}

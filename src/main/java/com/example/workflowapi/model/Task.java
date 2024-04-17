package com.example.workflowapi.model;

import com.example.workflowapi.enums.TaskPriority;
import com.example.workflowapi.enums.TaskStatus;
import com.example.workflowapi.enums.TaskType;
import com.example.workflowapi.validators.NotBlank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private TaskPriority priority;
    private LocalDateTime creationDate;
    private TaskType tasktype;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    @ManyToOne
    private User createdByUser;
//    @ManyToOne
//    private User assignedUser;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Comment> comments;
    private String attachment;
}

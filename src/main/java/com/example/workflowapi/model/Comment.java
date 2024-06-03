package com.example.workflowapi.model;

import com.example.workflowapi.validators.NotBlank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String content;
    @ManyToOne
    private WorkflowUser author;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private Task task;
    private Integer likes;
    private Integer unlikes;
    private LocalDate creationDate;
}

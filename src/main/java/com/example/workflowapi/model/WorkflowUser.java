package com.example.workflowapi.model;

import com.example.workflowapi.validators.Email;
import com.example.workflowapi.validators.NotBlank;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class WorkflowUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @Email
    private String email;
    private Boolean isActive;
    @OneToMany(mappedBy = "createdBy")
    private List<Task> createdTasks;
    @OneToMany(mappedBy = "assignedTo")
    private List<Task> assignedTasks;
    @OneToMany/*(mappedBy = "author")*/
    private List<Comment> comments;
}

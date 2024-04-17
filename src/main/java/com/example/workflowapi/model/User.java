package com.example.workflowapi.model;

import com.example.workflowapi.validators.Email;
import com.example.workflowapi.validators.NotBlank;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank
    private String username;
    @Email
    private String email;
    @Column(columnDefinition = "BOOLEAN")
    private Boolean isActive;
}

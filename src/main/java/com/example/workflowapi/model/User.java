package com.example.workflowapi.model;

import com.example.workflowapi.validators.NotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    private Boolean isActive;
}

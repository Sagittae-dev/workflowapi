package com.example.workflowapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private User author;
    private Integer likes;
    private Integer unlikes;
    private LocalDate creationDate;
}

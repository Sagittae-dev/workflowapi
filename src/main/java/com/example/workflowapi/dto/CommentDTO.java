package com.example.workflowapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDate creationDate;
    private String author;
    private Long taskId;
    private Integer likes;
    private Integer unlikes;
}

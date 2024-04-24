package com.example.workflowapi.controllers;

import com.example.workflowapi.dto.CommentDTO;
import com.example.workflowapi.exceptions.InvalidTaskTypeException;
import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForTask(@PathVariable Long taskId) {
        try {
            List<CommentDTO> commentsList = commentService.getAllCommentsForTask(taskId);
            return ResponseEntity.ok(commentsList);
        } catch (ResourceNotExistException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentDTO>> searchCommentsByContent(@RequestParam String searchString) {
        List<CommentDTO> commentsList = commentService.searchCommentsByContent(searchString);
        return ResponseEntity.ok(commentsList);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<CommentDTO> addCommentToTask(@PathVariable Long taskId, @RequestParam String username, @RequestBody String content) {
        try {
            CommentDTO comment = commentService.addCommentToTask(taskId, username, content);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotExistException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // TODO write proper logic to adding comments
        } catch (InvalidTaskTypeException ite) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

    }
}
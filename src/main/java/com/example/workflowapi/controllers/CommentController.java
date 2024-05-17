package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class
CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<Comment>> getAllCommentsForTask(@PathVariable Long taskId) {
            List<Comment> commentsList = commentService.getAllCommentsForTask(taskId);
            return ResponseEntity.ok(commentsList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Comment>> searchCommentsByContent(@RequestParam String searchString) {
        List<Comment> commentsList = commentService.searchCommentsByContent(searchString);
        return ResponseEntity.ok(commentsList);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Comment> addCommentToTask(@PathVariable Long taskId, @RequestParam Long userId, @RequestBody String content) {
        try {
            Comment comment = commentService.addCommentToTask(taskId, userId, content);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotExistException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // TODO write proper logic to adding comments
        }
    }
}
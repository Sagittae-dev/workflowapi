package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.Comment;
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
    public ResponseEntity<List<Comment>> getAllCommentsForTask(@PathVariable Long taskId) throws ResourceNotExistException {
        List<Comment> commentsList = commentService.getAllCommentsForTask(taskId);
        return ResponseEntity.ok(commentsList);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Comment>> searchCommentsByContent(@RequestParam String searchString) {
        List<Comment> commentsList = commentService.searchCommentsByContent(searchString);
        if(commentsList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(commentsList);
    }
}
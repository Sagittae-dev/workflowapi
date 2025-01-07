package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.AlreadyLikedException;
import com.example.workflowapi.exceptions.NotLikedException;
import com.example.workflowapi.exceptions.ResourceNotFoundException;
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
public class CommentController {

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
    public ResponseEntity<List<Comment>> searchCommentsInTaskByContent(@RequestParam Long taskId, @RequestBody String searchString) throws ResourceNotFoundException, IllegalArgumentException {
        List<Comment> commentsList = commentService.searchCommentsInTaskByContent(taskId, searchString);
        return ResponseEntity.ok(commentsList);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Comment> addCommentToTask(@PathVariable Long taskId, @RequestParam Long userId, @RequestBody String content) {
        try {
            Comment comment = commentService.addCommentToTask(taskId, userId, content);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // TODO write proper logic to adding comments
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> removeComment(@PathVariable Long commentId) throws ResourceNotFoundException {
        commentService.removeComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable Long commentId, @RequestParam Long userId) throws ResourceNotFoundException, AlreadyLikedException {
            Comment comment = commentService.likeComment(commentId, userId);
            return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}/unlike")
    public ResponseEntity<Comment> unlikeComment(@PathVariable Long commentId, @RequestParam Long userId) throws ResourceNotFoundException, NotLikedException {
            Comment comment = commentService.unlikeComment(commentId, userId);
            return ResponseEntity.ok(comment);
    }
}
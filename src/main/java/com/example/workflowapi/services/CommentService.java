package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.repositories.UserRepository;
import com.example.workflowapi.validators.CommentValidator;
import com.example.workflowapi.validators.ValidationResult;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentValidator commentValidator;
    private Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository, CommentValidator commentValidator) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentValidator = commentValidator;
    }

    public List<Comment> getAllCommentsForTask(Long taskId) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow();
            return task.getComments();
        } catch (NoSuchElementException re) {
            return Collections.emptyList();
        }
    }

    public Comment getCommentById(Long id) throws ResourceNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ResourceNotFoundException("Comment with id:" + id + " doesn't exist.");
        }
        return comment.get();
    }

    public Comment addCommentToTask(Long taskId, Long userId, String content) throws ValidationException, ResourceNotFoundException {
        WorkflowUser workflowUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " doesn't exist"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found for id: " + taskId));

        Comment comment = createComment(task, workflowUser, content);

        ValidationResult result = commentValidator.validate(comment);
        if (!result.isValid()) {
            throw new ValidationException(result.getErrors());
        }
        task.getComments().add(comment);

        return commentRepository.save(comment);
    }

    public List<Comment> searchCommentsByContent(String searchString) {
        return commentRepository.findByContentContainingIgnoreCase(searchString);
    }

    @Transactional
    public ResponseEntity<?> removeComment(Long commentId) {
        try {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            commentRepository.deleteById(commentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting the comment.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the comment.");
        }
    }

    private Comment createComment(Task task, WorkflowUser workflowUser, String content) {
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setAuthor(workflowUser);
        comment.setLikes(0);
        comment.setUnlikes(0);
        comment.setCreationDate(LocalDate.now());
        return comment;
    }


}
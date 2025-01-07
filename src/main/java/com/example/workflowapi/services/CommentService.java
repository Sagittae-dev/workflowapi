package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.AlreadyLikedException;
import com.example.workflowapi.exceptions.NotLikedException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentValidator commentValidator;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

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
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id:" + id + " doesn't exist."));
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

    public List<Comment> searchCommentsInTaskByContent(Long taskId, String searchString) throws ResourceNotFoundException, IllegalArgumentException {
        if (searchString == null || searchString.isBlank()) {
            throw new IllegalArgumentException("Search string cannot be null or empty");
        }
        String lowerCaseSearchString = searchString.toLowerCase(Locale.ROOT);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " doesn't exist."));
        return task.getComments()
                .stream()
                .filter(comment -> comment.getContent().toLowerCase().contains(lowerCaseSearchString))
                .toList();

    }

    @Transactional
    public void removeComment(Long commentId) throws ResourceNotFoundException {
        commentRepository.findById(commentId)
                .orElseThrow( () -> new ResourceNotFoundException("Comment with id: " + commentId + " doesn't exist."));
        commentRepository.deleteById(commentId);
    }

    private Comment createComment(Task task, WorkflowUser workflowUser, String content) {
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setAuthor(workflowUser);
        comment.setLikes(0);
        comment.setCreationDate(LocalDate.now());
        return comment;
    }

    @Transactional
    public Comment likeComment(Long commentId, Long userId) throws ResourceNotFoundException, AlreadyLikedException {
        Comment comment = getCommentById(commentId);
        Set<Long> likedBy = comment.getLikedBy();
        if(likedBy.contains(userId)) {
            throw new AlreadyLikedException("You have already liked this comment.");
        }
        comment.setLikes(comment.getLikes() + 1);
        likedBy.add(userId);
        comment.setLikedBy(likedBy);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment unlikeComment(Long commentId, Long userId) throws ResourceNotFoundException, NotLikedException {
        Comment comment = getCommentById(commentId);
        Set<Long> likedBy = comment.getLikedBy();
        if(!likedBy.contains(userId)) {
            throw new NotLikedException("You have not liked this comment.");
        }
        comment.setLikes(comment.getLikes() - 1);
        likedBy.remove(userId);
        comment.setLikedBy(likedBy);
        return commentRepository.save(comment);
    }
}
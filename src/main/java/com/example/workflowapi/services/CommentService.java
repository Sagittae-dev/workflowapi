package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.repositories.UserRepository;
import com.example.workflowapi.validators.CommentValidator;
import com.example.workflowapi.validators.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentValidator commentValidator;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository, CommentValidator commentValidator) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentValidator = commentValidator;
    }

    public List<Comment> getAllCommentsForTask(Long taskId) throws ResourceNotExistException {
        Task task = taskRepository.findById(taskId).orElseThrow();
//        return task.getComments().stream().map(CommentDTOMapper::mapToDTO).toList();
        return task.getComments();
    }

    public Comment getCommentById(Long id) throws ResourceNotExistException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ResourceNotExistException("Comment with id:" + id + " doesn't exist.");
        }
        return comment.get();
    }

    public Comment addCommentToTask(Long taskId, String username, String content) throws ValidationException, ResourceNotExistException {
        WorkflowUser workflowUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotExistException("User with username: " + username + " doesn't exist"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotExistException("No task found for id: " + taskId));

        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setContent(content);
        comment.setAuthor(workflowUser);
        comment.setLikes(0);
        comment.setUnlikes(0);
        comment.setCreationDate(LocalDate.now());
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
}
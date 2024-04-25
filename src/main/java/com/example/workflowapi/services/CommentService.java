package com.example.workflowapi.services;

import com.example.workflowapi.dto.CommentDTO;
import com.example.workflowapi.dto.TaskDTO;
import com.example.workflowapi.dtomapper.CommentDTOMapper;
import com.example.workflowapi.exceptions.InvalidTaskTypeException;
import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.validators.CommentValidator;
import com.example.workflowapi.validators.ValidationResult;
import com.example.workflowapi.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, TaskService taskService, UserService userService) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    public List<CommentDTO> getAllCommentsForTask(Long taskId) throws ResourceNotExistException {
        TaskDTO task = taskService.getTaskById(taskId);

        return task.getComments();
    }

    public Comment getCommentById(Long id) throws ResourceNotExistException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ResourceNotExistException("Comment with id:" + id + " doesn't exist.");
        }
        return comment.get();
    }

    public CommentDTO addCommentToTask(Long taskId, String username, String content) throws ValidationException, ResourceNotExistException, InvalidTaskTypeException {
        WorkflowUser workflowUser = userService.findUserByUsername(username);

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotExistException("No task found for id: " + taskId));
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setAuthor(workflowUser);
        comment.setTask(task);
        comment.setLikes(0);
        comment.setUnlikes(0);
        comment.setCreationDate(LocalDate.now());
        Validator<Comment> validator = new CommentValidator();
        ValidationResult result = validator.validate(comment);
        if (!result.isValid()) {
            throw new ValidationException(result.getErrors());
        }
        task.getComments().add(comment);
//        taskService.saveTask(task);

        return CommentDTOMapper.mapToDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> searchCommentsByContent(String searchString) {
        return commentRepository.findByContentContainingIgnoreCase(searchString)
                .stream()
                .map(CommentDTOMapper::mapToDTO)
                .toList();
    }
}
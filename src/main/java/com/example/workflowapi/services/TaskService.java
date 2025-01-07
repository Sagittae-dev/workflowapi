package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.repositories.UserRepository;
import com.example.workflowapi.validators.TaskValidator;
import com.example.workflowapi.validators.ValidationResult;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskValidator taskValidator, CommentRepository commentRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) throws ResourceNotFoundException {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throwResourceNotFoundException(id);
        }
        return optionalTask.get();
    }

    public Task createTask(Task task) throws ValidationException {
        task.setCreationDate(LocalDateTime.now());
        ValidationResult validationResult = taskValidator.validate(task);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task taskDetails) throws ResourceNotFoundException {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow( () -> new ResourceNotFoundException("Task with id: " + taskId + " not found."));
        if (taskDetails.getAssignedTo() != null) {
            existingTask.setAssignedTo(taskDetails.getAssignedTo());
        }
        if (taskDetails.getDueDate() != null) {
            existingTask.setDueDate(taskDetails.getDueDate());
        }
        if (StringUtils.hasText(taskDetails.getDescription())) {
            existingTask.setDescription(taskDetails.getDescription());
        }
        if (StringUtils.hasText(taskDetails.getName())) {
            existingTask.setName(taskDetails.getName());
        }
        if (taskDetails.getTaskType() != null) {
            existingTask.setTaskType(taskDetails.getTaskType());
        }
        if (taskDetails.getPriority() != null) {
            existingTask.setPriority(taskDetails.getPriority());
        }
        if (StringUtils.hasText(taskDetails.getAttachment())) {
            existingTask.setAttachment(taskDetails.getAttachment());
        }
        if (taskDetails.getTaskStatus() != null) {
            existingTask.setTaskStatus(taskDetails.getTaskStatus());
        }
        return taskRepository.save(existingTask);
    }

    @Transactional
    public void removeTask(Long taskId) {
        taskRepository.deleteById(taskId);
        commentRepository.deleteByTaskId(taskId);
    }

    public Task assignTask(Long taskId, Long userId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow( () -> new ResourceNotFoundException("Task with id: " + taskId + " not found."));
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow( () -> new ResourceNotFoundException("User with id: " + userId + " not found."));
        task.setAssignedTo(user);

        return taskRepository.save(task);
    }

    private void throwResourceNotFoundException(Long id) throws ResourceNotFoundException {
        throw new ResourceNotFoundException("Task with id:" + id + " not found.");
    }
}
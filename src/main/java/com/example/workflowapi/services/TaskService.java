package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.validators.TaskValidator;
import com.example.workflowapi.validators.ValidationResult;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final CommentRepository commentRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskValidator taskValidator, CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
        this.commentRepository = commentRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

//    public List<Task> getTasksByAssignee(User user){
//        return taskRepository.getTasksListByAssignee(user);
//    }

    public Task getTaskById(Long id) throws ResourceNotExistException {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throwResourceNotFoundException(id);
        }
        return optionalTask.get();
    }

    public Task saveTask(Task task) throws ValidationException {
        ValidationResult validationResult = taskValidator.validate(task);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) throws ResourceNotExistException {
        long taskId = task.getId();
        Optional<Task> taskToEdit = taskRepository.findById(taskId);
        if (taskToEdit.isEmpty()) {
            throwResourceNotFoundException(taskId);
        }
        return taskRepository.save(task);
    }

    @Transactional
    public void removeTask(Long taskId) {
        taskRepository.deleteById(taskId);
        commentRepository.deleteByTaskId(taskId);
    }

    private void throwResourceNotFoundException(Long id) throws ResourceNotExistException {
        throw new ResourceNotExistException("Task with id:" + id + " not found.");
    }
}
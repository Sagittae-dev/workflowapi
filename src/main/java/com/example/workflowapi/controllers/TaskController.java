package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            Task createdTask = taskService.saveTask(task);
            return ResponseEntity.ok(createdTask);
        } catch (ValidationException ve) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ve.getErrors());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (ResourceNotExistException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{taskId}")
    public void removeTask(@PathVariable Long taskId){
        taskService.removeTask(taskId);
    }
}
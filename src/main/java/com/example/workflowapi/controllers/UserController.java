package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.exceptions.UserAlreadyExistsException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<WorkflowUser>> getAllUsers() {
        List<WorkflowUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowUser> getUserById(@PathVariable Long id) {
        try {
            WorkflowUser workflowUser = userService.getUserById(id);
            return ResponseEntity.ok(workflowUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable Long id) throws ResourceNotFoundException {
        List<Task> tasks = userService.getTasksForUser(id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<WorkflowUser> createNewUser(@RequestBody WorkflowUser workflowUser) throws UserAlreadyExistsException {
        WorkflowUser createdWorkflowUser = userService.addUser(workflowUser);
        return ResponseEntity.ok(createdWorkflowUser);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<WorkflowUser> changePassword(@PathVariable Long id, @RequestBody String password) throws ResourceNotFoundException {
        WorkflowUser updatedUser = userService.changePassword(id, password);
        return ResponseEntity.ok(updatedUser);
    }
}
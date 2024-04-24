package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowUser> getUserById(@PathVariable Long id) {
        try {
            WorkflowUser workflowUser = userService.getUserById(id);
            return ResponseEntity.ok(workflowUser);
        } catch (ResourceNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<WorkflowUser> createNewUser(@RequestBody WorkflowUser workflowUser) {
        WorkflowUser createdWorkflowUser = userService.addUser(workflowUser);
        return ResponseEntity.ok(createdWorkflowUser);
    }
}
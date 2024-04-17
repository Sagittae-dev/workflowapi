package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.User;
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

    @GetMapping
    public ResponseEntity<User> getUserById(@PathVariable Long userId){
        try{
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch(ResourceNotExistException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<User> createNewUser(@RequestBody User user){
        User createdUser = userService.addUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
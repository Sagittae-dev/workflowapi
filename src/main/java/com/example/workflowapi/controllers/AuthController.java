package com.example.workflowapi.controllers;

import com.example.workflowapi.cofiguration.SecurityUtils;
import com.example.workflowapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username")
    public ResponseEntity<String> getAuthenticatedUsername() {
        String username = SecurityUtils.getAuthenticatedUsername();
        return ResponseEntity.ok(username);
    }
}

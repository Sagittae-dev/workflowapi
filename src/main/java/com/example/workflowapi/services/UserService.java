package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.exceptions.UserAlreadyExistsException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<WorkflowUser> getAllUsers() {
        return userRepository.findAll();
    }

    public WorkflowUser getUserById(Long id) throws ResourceNotFoundException {
        Optional<WorkflowUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User with id: " + id + " doesn't exist");
        }
        return optionalUser.get();
    }

    public WorkflowUser addUser(WorkflowUser workflowUser) throws UserAlreadyExistsException {
        String username = workflowUser.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        String email = workflowUser.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        return userRepository.save(workflowUser);
    }

    public WorkflowUser findUserByUsername(String username) throws ResourceNotFoundException {
        Optional<WorkflowUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("There is no user with this username");
        }
        return userOptional.get();
    }

    public List<Task> getTasksForUser(Long id) throws ResourceNotFoundException {
        WorkflowUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found."));
        return user.getAssignedTasks();
    }

    public WorkflowUser changePassword(Long id, String password) throws ResourceNotFoundException {
        WorkflowUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
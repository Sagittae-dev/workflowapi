package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public WorkflowUser addUser(WorkflowUser workflowUser) {
        return userRepository.save(workflowUser);
    }

    public WorkflowUser findUserByUsername(String username) throws ResourceNotFoundException {
        Optional<WorkflowUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("There is no user with this username");
        }
        return userOptional.get();
    }
}

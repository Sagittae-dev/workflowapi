package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.User;
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

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id) throws ResourceNotExistException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new ResourceNotExistException("User with id: " +id+ " doesn't exist");
        }
        return optionalUser.get();
    }
    public User addUser(User user){
        return userRepository.save(user);
    }
}

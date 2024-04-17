package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskService taskService){
        this.commentRepository = commentRepository;
        this.taskService = taskService;
    }

    public List<Comment> getAllCommentsForTask(Long taskId) throws ResourceNotExistException {
        Task task = taskService.getTaskById(taskId);

        return task.getComments();
    }

    public Comment getCommentById(Long id) throws ResourceNotExistException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()){
            throw new ResourceNotExistException("Comment with id:" + id +" doesn't exist.");
        }
        return comment.get();
    }

    public List<Comment> searchCommentsByContent(String searchString) {
        return commentRepository.findByContentContainingIgnoreCase(searchString);
    }
}
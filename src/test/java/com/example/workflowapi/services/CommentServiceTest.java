package com.example.workflowapi.services;

import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.repositories.UserRepository;
import com.example.workflowapi.validators.CommentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CommentValidator commentValidator;

    private CommentService commentService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, taskRepository, userRepository, commentValidator);
    }

    @Test
    void getAllCommentsForTask() {
    }

    @Test
    void getCommentById() {
    }

    @Test
    void addCommentToTask() {
    }

    @Test
    void searchCommentsByContent_Success() {

    }
}
package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentService);
    }

    @Test
    void getAllCommentsForTask_Success() {
        Long taskId = 1L;
        List<Comment> comments = List.of(new Comment(), new Comment());
        when(commentService.getAllCommentsForTask(taskId)).thenReturn(comments);

        ResponseEntity<List<Comment>> response = commentController.getAllCommentsForTask(taskId);

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(comments, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllCommentsForTask_NotFound() {
        Long taskId = 1L;
        when(commentService.getAllCommentsForTask(taskId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Comment>> response = commentController.getAllCommentsForTask(taskId);

        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void searchCommentsByContent_Success() {
        List<Comment> comments = List.of(new Comment(), new Comment());
        when(commentService.searchCommentsByContent("abc")).thenReturn(comments);
        ResponseEntity<List<Comment>> response = commentController.searchCommentsByContent("abc");

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentToTask_Success() throws ValidationException, ResourceNotExistException {
        Comment comment = new Comment();
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenReturn(comment);
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, "abc", "abcd");
        assertEquals(comment, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentToTask_ResourceNotExist() throws ValidationException, ResourceNotExistException {
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenThrow(new ResourceNotExistException("abc"));
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, "abc", "abcd");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addCommentToTask_NotValid() throws ValidationException, ResourceNotExistException {
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenThrow(new ValidationException(Collections.singletonList("abc")));
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, "abc", "abcd");

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
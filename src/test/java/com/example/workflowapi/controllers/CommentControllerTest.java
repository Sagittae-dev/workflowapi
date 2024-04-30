package com.example.workflowapi.controllers;

import com.example.workflowapi.dto.CommentDTO;
import com.example.workflowapi.exceptions.InvalidTaskTypeException;
import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
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
    void getAllCommentsForTask_Success() throws ResourceNotExistException {
        Long taskId = 1L;
        List<CommentDTO> comments = List.of(new CommentDTO(), new CommentDTO());
        when(commentService.getAllCommentsForTask(taskId)).thenReturn(comments);

        ResponseEntity<List<CommentDTO>> response = commentController.getAllCommentsForTask(taskId);

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(comments, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllCommentsForTask_NotFound() throws ResourceNotExistException {
        Long taskId = 1L;
        when(commentService.getAllCommentsForTask(taskId)).thenThrow(new ResourceNotExistException("No task with id: " + taskId));

        ResponseEntity<List<CommentDTO>> response = commentController.getAllCommentsForTask(taskId);

        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchCommentsByContent_Success() {
        List<CommentDTO> comments = List.of(new CommentDTO(), new CommentDTO());
        when(commentService.searchCommentsByContent("abc")).thenReturn(comments);
        ResponseEntity<List<CommentDTO>> response = commentController.searchCommentsByContent("abc");

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentToTask_Success() throws ValidationException, ResourceNotExistException, InvalidTaskTypeException {
        CommentDTO comment = new CommentDTO();
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenReturn(comment);
        ResponseEntity<CommentDTO> response = commentController.addCommentToTask(1L, "abc", "abcd");
        assertEquals(comment, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentToTask_ResourceNotExist() throws ValidationException, ResourceNotExistException, InvalidTaskTypeException {
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenThrow(new ResourceNotExistException("abc"));
        ResponseEntity<CommentDTO> response = commentController.addCommentToTask(1L, "abc", "abcd");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddCommentToTask_InvalidTaskTypeException() throws ValidationException, ResourceNotExistException, InvalidTaskTypeException {
        when(commentService.addCommentToTask(anyLong(), anyString(), anyString())).thenThrow(InvalidTaskTypeException.class);
        CommentController controller = new CommentController(commentService);

        ResponseEntity<CommentDTO> response = controller.addCommentToTask(1L, "username", "content");

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

}
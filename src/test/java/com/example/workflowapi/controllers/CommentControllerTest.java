package com.example.workflowapi.controllers;

import com.example.workflowapi.exceptions.*;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CommentService commentService;

    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void getAllCommentsForTask_Success() throws ResourceNotFoundException {
        Long taskId = 1L;
        List<Comment> comments = List.of(new Comment(), new Comment());
        when(commentService.getAllCommentsForTask(taskId)).thenReturn(comments);

        ResponseEntity<List<Comment>> response = commentController.getAllCommentsForTask(taskId);

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(comments, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllCommentsForTask_NotFound() throws ResourceNotFoundException {
        Long taskId = 1L;
        when(commentService.getAllCommentsForTask(taskId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Comment>> response = commentController.getAllCommentsForTask(taskId);

        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void searchCommentsByContent_Success() throws ResourceNotFoundException {
        List<Comment> comments = List.of(new Comment(), new Comment());
        when(commentService.searchCommentsInTaskByContent(1L, "abc")).thenReturn(comments);
        ResponseEntity<List<Comment>> response = commentController.searchCommentsInTaskByContent(1L, "abc");

        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void searchCommentsByContent_NotFound() throws ResourceNotFoundException {
        when(commentService.searchCommentsInTaskByContent(1L, "abc")).thenReturn(Collections.emptyList());
        ResponseEntity<List<Comment>> response = commentController.searchCommentsInTaskByContent(1L, "abc");

        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void searchCommentsByContent_ThrowsIllegalArgumentException() throws ResourceNotFoundException, Exception {
        when(commentService.searchCommentsInTaskByContent(anyLong(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid search string"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/search")
                        .param("taskId", "1")
                        .content("abc"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void addCommentToTask_Success() throws ValidationException, ResourceNotFoundException {
        Comment comment = new Comment();
        when(commentService.addCommentToTask(anyLong(), anyLong(), anyString())).thenReturn(comment);
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, 2L, "abcd");
        assertEquals(comment, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentToTask_ResourceNotExist() throws ValidationException, ResourceNotFoundException {
        when(commentService.addCommentToTask(anyLong(), anyLong(), anyString())).thenThrow(new ResourceNotFoundException("abc"));
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, 2L, "abcd");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addCommentToTask_NotValid() throws ValidationException, ResourceNotFoundException {
        when(commentService.addCommentToTask(anyLong(), anyLong(), anyString())).thenThrow(new ValidationException(Collections.singletonList("abc")));
        ResponseEntity<Comment> response = commentController.addCommentToTask(1L, 2L, "abcd");

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void removeComment_Success() throws ResourceNotFoundException {
        ResponseEntity<?> response = commentController.removeComment(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void removeComment_ResourceNotFound() throws ResourceNotFoundException, Exception {
        doThrow(new ResourceNotFoundException("Comment with id: 1 doesn't exist."))
                .when(commentService).removeComment(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void likeComment_Success() throws ResourceNotFoundException {
        Comment comment = new Comment();
        when(commentService.likeComment(anyLong(), anyLong())).thenReturn(comment);
        ResponseEntity<Comment> response = commentController.likeComment(1L, 2L);
        assertEquals(comment, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void likeComment_ResourceNotFound() throws ResourceNotFoundException, Exception {
        when(commentService.likeComment(anyLong(), anyLong())).thenThrow(new ResourceNotFoundException("Comment with id: 1 doesn't exist."));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/comments/1/like")
                        .param("userId", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void likeComment_AlreadyLiked() throws ResourceNotFoundException, Exception {
        when(commentService.likeComment(anyLong(), anyLong())).thenThrow(new AlreadyLikedException("You have already liked this comment."));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/comments/1/like")
                        .param("userId", "2"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void unlikeComment_Success() throws ResourceNotFoundException {
        Comment comment = new Comment();
        when(commentService.unlikeComment(anyLong(), anyLong())).thenReturn(comment);
        ResponseEntity<Comment> response = commentController.unlikeComment(1L, 2L);
        assertEquals(comment, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void unlikeComment_ResourceNotFound() throws ResourceNotFoundException, Exception {
        when(commentService.unlikeComment(anyLong(), anyLong())).thenThrow(new ResourceNotFoundException("Comment with id: 1 doesn't exist."));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/comments/1/unlike")
                        .param("userId", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void unlikeComment_NotLiked() throws ResourceNotFoundException, Exception {
        when(commentService.unlikeComment(anyLong(), anyLong())).thenThrow(new NotLikedException("You have not liked this comment."));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/comments/1/unlike")
                        .param("userId", "2"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
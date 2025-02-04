package com.example.workflowapi.services;

import com.example.workflowapi.exceptions.AlreadyLikedException;
import com.example.workflowapi.exceptions.ResourceNotFoundException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Comment;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.model.WorkflowUser;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.repositories.UserRepository;
import com.example.workflowapi.validators.CommentValidator;
import com.example.workflowapi.validators.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

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
    void getAllCommentsForTask_Ok() throws ResourceNotFoundException {
        Mockito.when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(new Task()));
        commentService.getAllCommentsForTask(1L);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getAllCommentsForTask_ThrowsException()  {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.getAllCommentsForTask(1L));
    }

    @Test
    void getCommentById_Ok() throws ResourceNotFoundException {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(new Comment()));
        commentService.getCommentById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getCommentById_ThrowsException() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L));
    }

    @Test
    void addCommentToTask_Ok() throws ValidationException, ResourceNotFoundException {
        Task task = new Task();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new WorkflowUser()));
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(commentValidator.validate(Mockito.any())).thenReturn(new ValidationResult(true, null));
        commentService.addCommentToTask(1L, 1L, "content");
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentValidator, Mockito.times(1)).validate(Mockito.any());
        Assertions.assertEquals(1, task.getComments().size());
        Assertions.assertEquals("content", task.getComments().getFirst().getContent());
    }

    @Test
    void addCommentToTask_ThrowsValidationException() throws ResourceNotFoundException {
        Task task = new Task();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new WorkflowUser()));
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(commentValidator.validate(Mockito.any())).thenReturn(new ValidationResult(false, null));
        Assertions.assertThrows(ValidationException.class,
                () -> commentService.addCommentToTask(1L, 1L, "content"));
    }

    @Test
    void addCommentToTask_ThrowsResourceNotFoundException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.addCommentToTask(1L, 1L, "content"));
    }

    @Test
    void searchCommentsByContent_Success() throws ResourceNotFoundException {
        Task task = new Task();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("content1");
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("content2");
        task.setId(1L);
        task.setComments(List.of(comment1, comment2));
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        List<Comment> comments = commentService.searchCommentsInTaskByContent(1L, "content1");
        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(1L, comments.getFirst().getId());
        Assertions.assertEquals("content1", comments.getFirst().getContent());
    }

    @Test
    void searchCommentsByContent_ThrowsResourceNotFoundException() {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.searchCommentsInTaskByContent(1L, "content1"));
    }
    @Test
    void searchCommentsByContent_ThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentService.searchCommentsInTaskByContent(1L, ""));
    }

    @Test
    void removeComment_Ok() throws ResourceNotFoundException {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(new Comment()));
        commentService.removeComment(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }
    @Test
    void removeComment_ThrowsResourceNotFoundException() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.removeComment(1L));
    }
    @Test
    void likeComment_Ok() throws ResourceNotFoundException {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setLikes(1);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        commentService.likeComment(1L, 3L);
        Mockito.verify(commentRepository, Mockito.times(1)).findById(1L);
        Assertions.assertEquals(2, comment.getLikes());
        Assertions.assertTrue(comment.getLikedBy().contains(3L));
    }

    @Test
    void likeComment_ThrowsResourceNotFoundException() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.likeComment(1L, 3L));
    }

    @Test
    void likeComment_ThrowsAlreadyLikedException() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setLikes(1);
        comment.getLikedBy().add(3L);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        Assertions.assertThrows(AlreadyLikedException.class,
                () -> commentService.likeComment(1L, 3L));
    }

    @Test
    void unlikeComment_Ok() throws ResourceNotFoundException {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setLikes(1);
        comment.getLikedBy().add(3L);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        commentService.unlikeComment(1L, 3L);
        Mockito.verify(commentRepository, Mockito.times(1)).findById(1L);
        Assertions.assertEquals(0, comment.getLikes());
        Assertions.assertFalse(comment.getLikedBy().contains(3L));
    }

    @Test
    void unlikeComment_ThrowsResourceNotFoundException() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.unlikeComment(1L, 3L));
    }

}
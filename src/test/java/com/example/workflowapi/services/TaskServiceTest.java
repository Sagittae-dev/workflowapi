package com.example.workflowapi.services;

import com.example.workflowapi.enums.TaskType;
import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.exceptions.ValidationException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.repositories.CommentRepository;
import com.example.workflowapi.repositories.TaskRepository;
import com.example.workflowapi.validators.TaskValidator;
import com.example.workflowapi.validators.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskValidator taskValidatorMock;

    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.taskService = new TaskService(taskRepositoryMock, taskValidatorMock, commentRepository);
    }

    @Test
    void getAllTasks_MultipleTasks() {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskRepositoryMock.findAll()).thenReturn(tasks);
        List<Task> expectedTasks = taskService.getAllTasks();
        assertEquals(2, expectedTasks.size());
    }

    @Test
    void getAllTasks_OneTask() {
        List<Task> tasks = List.of(new Task());
        when(taskRepositoryMock.findAll()).thenReturn(tasks);
        List<Task> expectedTasks = taskService.getAllTasks();
        assertNotNull(expectedTasks);
        assertEquals(1, expectedTasks.size());
    }

    @Test
    void getAllTasks_NoTasks() {
        List<Task> tasks = Collections.emptyList();
        when(taskRepositoryMock.findAll()).thenReturn(tasks);
        List<Task> expectedTasks = taskService.getAllTasks();
        assertNotNull(expectedTasks);
        assertTrue(expectedTasks.isEmpty());
    }

    @Test
    void getTaskById_Success() throws ResourceNotExistException {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setName("task1");
        task.setTaskType(TaskType.BUG);
        task.setCreationDate(LocalDateTime.now());

        when(taskRepositoryMock.findById(taskId)).thenReturn(Optional.of(task));

        Task retrievedTask = taskService.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(task.getId(), retrievedTask.getId());
        assertEquals(task.getName(), retrievedTask.getName());
        assertEquals(task.getTaskType(), retrievedTask.getTaskType());
        assertEquals(task.getCreationDate(), retrievedTask.getCreationDate());
    }

    @Test
    void getTaskById_ResourceNotExist() {
        when(taskRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotExistException.class, () -> taskService.getTaskById(1L));
    }


    @Test
    void saveTask_Success() throws ValidationException {
        Task task = new Task();
        task.setName("task1");
        task.setTaskType(TaskType.BUG);
        task.setCreationDate(LocalDateTime.now());
        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(null);
        validationResult.setValid(true);

        when(taskValidatorMock.validate(task)).thenReturn(validationResult);

        when(taskRepositoryMock.save(task)).thenReturn(task);

        Task savedTask = taskService.saveTask(task);
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getName(), savedTask.getName());
        assertEquals(task.getTaskType(), savedTask.getTaskType());
        assertEquals(task.getCreationDate(), savedTask.getCreationDate());
    }

    @Test
    public void testSaveTask_ValidationFailure() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("This is a test task");

        List<String> errors = List.of("Name is required");
        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(errors);
        validationResult.setValid(false);


        when(taskRepositoryMock.save(task)).thenReturn(task);
        when(taskValidatorMock.validate(task)).thenReturn(validationResult);

        assertThrows(ValidationException.class, () -> taskService.saveTask(task));
    }

    @Test
    void updateTask() {
    }
}
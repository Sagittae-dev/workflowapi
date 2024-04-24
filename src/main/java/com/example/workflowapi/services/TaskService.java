package com.example.workflowapi.services;

import com.example.workflowapi.dto.TaskDTO;
import com.example.workflowapi.dtomapper.TaskDTOMapper;
import com.example.workflowapi.enums.TaskType;
import com.example.workflowapi.exceptions.InvalidTaskTypeException;
import com.example.workflowapi.exceptions.ResourceNotExistException;
import com.example.workflowapi.model.Task;
import com.example.workflowapi.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> taskList = taskRepository.findAll().stream().map(TaskDTOMapper::mapToDTO).toList();
        return taskList;
    }

//    public List<Task> getTasksByAssignee(User user){
//        return taskRepository.getTasksListByAssignee(user);
//    }

    public TaskDTO getTaskById(Long id) throws ResourceNotExistException {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throwResourceNotFoundException(id);
        }
        Task task = optionalTask.get();
        return TaskDTOMapper.mapToDTO(task);
    }

    public TaskDTO saveTask(Task task) throws InvalidTaskTypeException {
        //TODO extract all validation to different class TaskValidator

        Optional<TaskType> validTaskType = TaskType.fromString(task.getTaskType().toString());
        if (validTaskType.isPresent()) {
            task.setTaskType(validTaskType.get());
        } else {
            throw new InvalidTaskTypeException("Invalid task type: " + task.getTaskType());
        }
        return TaskDTOMapper.mapToDTO(taskRepository.save(task));
    }

    public Task updateTask(Task task) throws ResourceNotExistException {
        long taskId = task.getId();
        Optional<Task> taskToEdit = taskRepository.findById(taskId);
        if (taskToEdit.isEmpty()) {
            throwResourceNotFoundException(taskId);
        }
        return taskRepository.save(task);
    }

    private void throwResourceNotFoundException(Long id) throws ResourceNotExistException {
        throw new ResourceNotExistException("Task with id:" + id + " not found.");
    }
}
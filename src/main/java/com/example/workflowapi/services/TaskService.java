package com.example.workflowapi.services;

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

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

//    public List<Task> getTasksByAssignee(User user){
//        return taskRepository.getTasksListByAssignee(user);
//    }

    public Task getTaskById(Long id) throws ResourceNotExistException {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()){
            throwResourceNotFoundException(id);
        }
        return optionalTask.get();
    }

    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) throws ResourceNotExistException {
        long taskId = task.getId();
        Optional<Task> taskToEdit = taskRepository.findById(taskId);
        if(taskToEdit.isEmpty()){
            throwResourceNotFoundException(taskId);
        }
        return taskRepository.save(task);
    }

    private void throwResourceNotFoundException(Long id) throws ResourceNotExistException {
        throw new ResourceNotExistException("Task with id:" + id + " not found.");
    }
}
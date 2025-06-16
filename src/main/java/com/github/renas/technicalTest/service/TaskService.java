package com.github.renas.technicalTest.service;

import com.github.renas.technicalTest.controller.ResourceNotFoundException;
import com.github.renas.technicalTest.controller.Task;
import com.github.renas.technicalTest.controller.TaskStatus;
import com.github.renas.technicalTest.controller.TaskRequest;
import com.github.renas.technicalTest.persistance.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class TaskService {

    private final TaskRepo taskRepo;


    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;

    }

    public Task getById(UUID id) {
        return taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
    }

    public List<Task> getAll() {
        return taskRepo.findAll();
    }

    @Transactional
    public Task create(TaskRequest task) {
        Task createdTask = new Task();

        createdTask.setUuid(UUID.randomUUID());
        createdTask.setTitle(task.name());
        createdTask.setDescription(task.description());
        createdTask.setDueDate(task.dueDate());
        createdTask.setStatus(TaskStatus.INCOMPLETE);

        return taskRepo.save(createdTask);

    }

    public void delete(UUID id) {
        if (!taskRepo.existsById(id)) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        taskRepo.deleteById(id);
    }

    public Task update(Task task) {
        if (!taskRepo.existsById((task.getUuid()))) {
            throw new ResourceNotFoundException("Task with ID " + task.getUuid() + " not found");
        }
        Task updatedTask = new Task();
        updatedTask.setUuid(task.getUuid());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        updatedTask.setTitle(task.getTitle());

        return taskRepo.save(updatedTask);
    }


    public Task changeStatus(UUID id, TaskStatus newStatus) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));

        task.setStatus(newStatus);

        return taskRepo.save(task);
    }

}



package com.github.renas.technicalTest.service;

import com.github.renas.technicalTest.ResourceNotFoundException;
import com.github.renas.technicalTest.Task;
import com.github.renas.technicalTest.TaskStatus;
import com.github.renas.technicalTest.controller.TaskRequest;
import com.github.renas.technicalTest.persistance.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
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

    @Transactional
    public Task create(TaskRequest task) {
        Task taskDao = new Task();

        taskDao.setUuid(UUID.randomUUID());
        taskDao.setDescription(task.description());
        taskDao.setDueDate(task.dueDate());
        taskDao.setStatus(TaskStatus.INCOMPLETE);

        return taskRepo.save(taskDao);
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
        return taskRepo.save(task);
        return saveAndConvertTaskDaoToTask(task, taskDao, task.getUuid());
    }

    private Task saveAndConvertTaskDaoToTask(Task task, TaskDao taskDao, UUID id) {
        taskDao.setUuid(id);
        taskDao.setUserId(getLoggedInUserId());
        taskDao.setName(task.name());
        taskDao.setDescription(task.description());
        taskDao.setEisenhower(task.eisenhowerMatrix());
        taskDao.setLabelId(task.labelId());
        taskDao.setCreatedDate(task.createdDate());
        taskDao.setDueDate(task.dueDate());
        taskDao.setCompletedDate(task.completedDate());
        taskDao.setTaskStatus(task.taskStatus());
        TaskDao createdTask = taskRepo.save(taskDao);
        return new Task(
                createdTask.getUuid(),
                createdTask.getName(),
                createdTask.getDescription(),
                createdTask.getEisenhower(),
                createdTask.getLabelId(),
                createdTask.getCreatedDate(),
                createdTask.getDueDate(),
                createdTask.getCompletedDate(),
                createdTask.getTaskStatus()

        );
    }

    public TaskStatus changeStatus(UUID id, TaskStatus newStatus) {

        TaskDao taskDao = taskRepo.findTaskByIdForCurrentUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));

        taskDao.setTaskStatus(newStatus);

        if (newStatus == TaskStatus.DONE) {
            taskDao.setCompletedDate(Date.valueOf(LocalDate.now()));
            xpService.addXP(Rewards.TASK_COMPLETED);
        } else {
            taskDao.setCompletedDate(null);
        }

        return taskRepo.save(taskDao).getTaskStatus();
    }

}



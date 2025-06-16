package com.github.renas.technicalTest.controller;


import com.github.renas.technicalTest.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest task) {
        return ResponseEntity.ok(taskService.create(task));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.update(task));
    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<Task> changeStatus(@PathVariable UUID id, @RequestBody TaskStatusRequest request) {
        return ResponseEntity.ok(taskService.changeStatus(id, request.newStatus()));
    }

}

package com.github.renas.technicalTest.service;

import com.github.renas.technicalTest.controller.ResourceNotFoundException;
import com.github.renas.technicalTest.controller.Task;
import com.github.renas.technicalTest.controller.TaskStatus;
import com.github.renas.technicalTest.controller.TaskRequest;
import com.github.renas.technicalTest.persistance.TaskRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private UUID testTaskId;

    @BeforeEach
    void setUp() {
        testTaskId = UUID.randomUUID();
        testTask = new Task();
        testTask.setUuid(testTaskId);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setDueDate(Date.valueOf(LocalDate.now()));
        testTask.setStatus(TaskStatus.INCOMPLETE);
    }

    @Test
    void getById_WhenTaskExists_ShouldReturnTask() {
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.of(testTask));
        Task foundTask = taskService.getById(testTaskId);
        assertNotNull(foundTask);
        assertEquals(testTaskId, foundTask.getUuid());
        verify(taskRepo).findById(testTaskId);
    }

    @Test
    void getById_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getById(testTaskId);
        });
        verify(taskRepo).findById(testTaskId);
    }

    @Test
    void create_ShouldCreateAndReturnTask() {
        TaskRequest request = new TaskRequest("New Task", "New Desc", Date.valueOf(LocalDate.now()));
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        when(taskRepo.save(taskArgumentCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        Task createdTask = taskService.create(request);
        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        assertEquals("New Desc", createdTask.getDescription());
        assertEquals(TaskStatus.INCOMPLETE, createdTask.getStatus());
        verify(taskRepo).save(any(Task.class));
    }

    @Test
    void delete_WhenTaskExists_ShouldDeleteTask() {
        when(taskRepo.existsById(testTaskId)).thenReturn(true);
        doNothing().when(taskRepo).deleteById(testTaskId);
        taskService.delete(testTaskId);
        verify(taskRepo).deleteById(testTaskId);
    }

    @Test
    void delete_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(taskRepo.existsById(testTaskId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.delete(testTaskId);
        });
        verify(taskRepo, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_WhenTaskExists_ShouldUpdateAndReturnTask() {
        Task updateDetails = new Task();
        updateDetails.setUuid(testTaskId);
        updateDetails.setTitle("Updated Title");
        updateDetails.setDescription("Updated Description");
        updateDetails.setStatus(TaskStatus.COMPLETE);
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.of(testTask));
        when(taskRepo.save(any(Task.class))).thenReturn(updateDetails);
        Task result = taskService.update(updateDetails);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskStatus.COMPLETE, result.getStatus());
        verify(taskRepo).findById(testTaskId);
        verify(taskRepo).save(any(Task.class));
    }

    @Test
    void update_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        Task updateDetails = new Task();
        updateDetails.setUuid(testTaskId);
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(updateDetails);
        });
        verify(taskRepo).findById(testTaskId);
        verify(taskRepo, never()).save(any(Task.class));
    }

    @Test
    void changeStatus_WhenTaskExists_ShouldChangeStatusAndReturnTask() {
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.of(testTask));
        when(taskRepo.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Task updatedTask = taskService.changeStatus(testTaskId, TaskStatus.COMPLETE);
        assertNotNull(updatedTask);
        assertEquals(TaskStatus.COMPLETE, updatedTask.getStatus());
        verify(taskRepo).findById(testTaskId);
        verify(taskRepo).save(testTask);
    }

    @Test
    void changeStatus_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(taskRepo.findById(testTaskId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.changeStatus(testTaskId, TaskStatus.COMPLETE);
        });
        verify(taskRepo).findById(testTaskId);
        verify(taskRepo, never()).save(any(Task.class));
    }
}

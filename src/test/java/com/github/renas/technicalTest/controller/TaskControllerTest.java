package com.github.renas.technicalTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.renas.technicalTest.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void getTask_ShouldReturnTask_WhenTaskExists() throws Exception {
        when(taskService.getById(testTaskId)).thenReturn(testTask);
        mockMvc.perform(get("/task/get/{id}", testTaskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(testTaskId.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskRequest taskRequest = new TaskRequest("New Task", "A description", Date.valueOf(LocalDate.now()));
        when(taskService.create(any(TaskRequest.class))).thenReturn(testTask);
        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void deleteTask_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(taskService).delete(testTaskId);
        mockMvc.perform(delete("/task/delete/{id}", testTaskId))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        when(taskService.update(any(Task.class))).thenReturn(testTask);
        mockMvc.perform(put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(testTaskId.toString()));
    }

    @Test
    void changeStatus_ShouldReturnNewStatus() throws Exception {
        TaskStatusRequest statusRequest = new TaskStatusRequest(TaskStatus.COMPLETE);
        testTask.setStatus(TaskStatus.COMPLETE);
        when(taskService.changeStatus(eq(testTaskId), any(TaskStatus.class))).thenReturn(testTask);
        mockMvc.perform(post("/task/change-status/{id}", testTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("COMPLETE"));
    }
}

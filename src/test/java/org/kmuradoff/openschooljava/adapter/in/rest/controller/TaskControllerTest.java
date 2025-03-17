package org.kmuradoff.openschooljava.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kmuradoff.openschooljava.CommonContainers;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Task Controller Integration Tests (SQL Initialization)")
public class TaskControllerTest extends CommonContainers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should retrieve all tasks successfully")
    public void getAllTasks_success() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Should create a new task successfully")
    public void createTask_success() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New description");
        newTask.setStatus(TaskStatus.IN_PROGRESS);
        newTask.setUserId("new-user");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return Bad Request when creating task without title")
    public void createTask_missingTitle_returnsBadRequest() throws Exception {
        Task testTask = new Task();
        testTask.setTitle(null);
        testTask.setDescription("New description");
        testTask.setStatus(TaskStatus.COMPLETED);
        testTask.setUserId("new-user");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should retrieve an existing task by ID successfully")
    public void getTaskById_success() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(999))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.userId").value("task1"));
    }

    @Test
    @DisplayName("Should return Not Found for non-existing task ID")
    public void getTaskById_notFound() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 555))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update an existing task successfully")
    public void updateTask_success() throws Exception {
        Task testTask = new Task();
        testTask.setTitle("New Title Updated");
        testTask.setDescription("New description");
        testTask.setStatus(TaskStatus.COMPLETED);
        testTask.setUserId("updated-user");

        mockMvc.perform(put("/tasks/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete an existing task successfully")
    public void deleteTask_success() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 999))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return Not Found when deleting non-existing task")
    public void deleteTask_notFound() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 111))
                .andExpect(status().isNotFound());
    }
}

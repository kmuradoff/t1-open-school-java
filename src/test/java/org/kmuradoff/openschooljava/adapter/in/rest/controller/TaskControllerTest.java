package org.kmuradoff.openschooljava.adapter.in.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kmuradoff.openschooljava.CommonContainers;
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
        String newTaskJson = """
                {
                    "title": "New Task",
                    "description": "New description",
                    "status": "IN_PROGRESS",
                    "userId": "new-user"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return Bad Request when creating task without title")
    public void createTask_missingTitle_returnsBadRequest() throws Exception {
        String invalidTaskJson = """
                {
                    "description": "Invalid task",
                    "status": "COMPLETED",
                    "userId": "user1"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTaskJson))
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
        String updatedTaskJson = """
                {
                    "title": "Updated Task 1",
                    "description": "Updated description",
                    "status": "COMPLETED",
                    "userId": "updated-user"
                }
                """;

        mockMvc.perform(put("/tasks/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Bad Request when updating task with invalid status")
    public void updateTask_invalidStatus_returnsBadRequest() throws Exception {
        String invalidUpdateJson = """
                {
                    "title": "Task",
                    "description": "Desc",
                    "status": "INVALID_STATUS",
                    "userId": "user1"
                }
                """;

        mockMvc.perform(put("/tasks/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateJson))
                .andExpect(status().isBadRequest());
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

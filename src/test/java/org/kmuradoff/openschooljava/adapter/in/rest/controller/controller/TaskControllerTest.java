package org.kmuradoff.openschooljava.adapter.in.rest.controller.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kmuradoff.openschooljava.CommonContainers;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
import org.kmuradoff.openschooljava.adapter.out.postgres.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Интеграционные тесты - Task Controller")
public class TaskControllerTest extends CommonContainers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        createTasks();
    }

    private void createTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Task 1");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setUserId("task1");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Task 2");
        task2.setStatus(TaskStatus.COMPLETED);
        task2.setUserId("task2");

        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setDescription("Task 3");
        task3.setStatus(TaskStatus.CANCELLED);
        task3.setUserId("task3");


        repository.saveAll(List.of(task1, task2, task3));
    }

    @Test
    @DisplayName("Создание задачи: Успешное создание задачи")
    public void createTask_success() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "New Task",
                                    "description": "New description",
                                    "status": "IN_PROGRESS",
                                    "userId": "new-user"
                                }
                                """))
                .andExpect(status().isCreated());

        List<Task> tasks = repository.findAll();
        assertEquals(4, tasks.size());
        Task createdTask = tasks.get(3);
        assertEquals("New Task", createdTask.getTitle());
        assertEquals("New description", createdTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, createdTask.getStatus());
        assertEquals("new-user", createdTask.getUserId());
    }

    @Test
    @DisplayName("Создание задачи: Ошибка при отсутствии заголовка")
    public void createTask_missingTitle_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "Invalid task",
                                    "status": "COMPLETED",
                                    "userId": "user1"
                                }
                                """))
                .andExpect(status().isBadRequest());

        assertEquals(3, repository.count());
    }

    @Test
    @DisplayName("Получение задачи по ID: Успешное получение существующей задачи")
    public void getTaskById_success() throws Exception {
        Task task = repository.findAll().get(0);

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.userId").value("task1"));
    }

    @Test
    @DisplayName("Получение задачи по ID: Ошибка при запросе несуществующей задачи")
    public void getTaskById_notFound() throws Exception {
        Long nonExistingId = 999L;

        mockMvc.perform(get("/tasks/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновление задачи: Успешное обновление данных задачи")
    public void updateTask_success() throws Exception {
        String updatedTitle = "Updated Task 1";

        mockMvc.perform(put("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "%s",
                                    "description": "Updated description",
                                    "status": "COMPLETED",
                                    "userId": "updated-user"
                                }
                                """.formatted(updatedTitle)))
                .andExpect(status().isOk());

        Task updatedTask = repository.findById(1L).orElseThrow();
        assertEquals(updatedTitle, updatedTask.getTitle());
        assertEquals("Updated description", updatedTask.getDescription());
        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
        assertEquals("updated-user", updatedTask.getUserId());
    }

    @Test
    @DisplayName("Обновление задачи: Ошибка при невалидном статусе")
    public void updateTask_invalidStatus_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Task",
                                    "description": "Desc",
                                    "status": "INVALID_STATUS",
                                    "userId": "user1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Удаление задачи: Успешное удаление существующей задачи")
    public void deleteTask_success() throws Exception {
        Task task = repository.findAll().get(0);

        mockMvc.perform(delete("/tasks/{id}",task.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(task.getId()));
    }

    @Test
    @DisplayName("Удаление задачи: Ошибка при удалении несуществующей задачи")
    public void deleteTask_notFound() throws Exception {
        Long nonExistingId = 999L;

        mockMvc.perform(delete("/tasks/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получение всех задач: Успешное получение списка задач")
    public void getAllTasks_success() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[2].title").value("Task 3"));
    }
}
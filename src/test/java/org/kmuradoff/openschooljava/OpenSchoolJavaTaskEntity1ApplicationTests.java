package org.kmuradoff.openschooljava;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kmuradoff.openschooljava.adapter.common.exception.CommonAdapterException;
import org.kmuradoff.openschooljava.adapter.in.rest.controller.TaskController;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.kmuradoff.openschooljava.application.port.in.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class OpenSchoolJavaTaskEntity1ApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешное создание Task и получение статуса 201")
    void createTask_Returns201() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Test", "Descriptions", "123jsd123ysad9-asdh732ijdfnnns");
        doNothing().when(taskService).createTask(any());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Получение Task по ID и возврат 200")
    void getTaskById_Returns200() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Test", "Descriptions", "123jsd123ysad9-asdh732ijdfnnns");
        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    @DisplayName("Обновление Task и возврат 200")
    void updateTask_Returns200() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Updated", "New Desc", "hash");
        doNothing().when(taskService).updateTask(any());

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление Task и возврат 204")
    void deleteTask_Returns200() throws Exception {
        doNothing().when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Получение всех Task и возврат 200")
    void getAllTasks_Returns200() throws Exception {
        List<TaskDto> tasks = List.of(
                new TaskDto(1L, "Test1", "Desc1", "hash1"),
                new TaskDto(2L, "Test2", "Desc2", "hash2")
        );
        when(taskService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }


    @Test
    @DisplayName("Ошибка при создании Task")
    void createTask_ThrowsError() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Test", "Descriptions", "hash");
        doThrow(new CommonAdapterException("Creation failed")).when(taskService).createTask(any());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Ошибка при получении Task по ID")
    void getTaskById_ThrowsError() throws Exception {
        when(taskService.getTaskById(1L)).thenThrow(new CommonAdapterException("Task not found"));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Ошибка при обновлении Task")
    void updateTask_ThrowsError() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Updated", "New Desc", "hash");
        doThrow(new CommonAdapterException("Update failed"))
                .when(taskService)
                .updateTask(any(TaskDto.class));

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Ошибка при удалении Task")
    void deleteTask_ThrowsError() throws Exception {
        doThrow(new CommonAdapterException("Delete failed")).when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Ошибка при получении списка Task")
    void getAllTasks_ThrowsError() throws Exception {
        when(taskService.getTasks()).thenThrow(new CommonAdapterException("Error fetching tasks"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isInternalServerError());
    }
}

package org.kmuradoff.openschooljava.application.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.adapter.out.postgres.mapper.TaskMapper;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.kmuradoff.openschooljava.application.domain.exception.BadRequestException;
import org.kmuradoff.openschooljava.application.domain.exception.NotFoundException;
import org.kmuradoff.openschooljava.application.port.out.KafkaProducerPort;
import org.kmuradoff.openschooljava.application.port.out.TaskPort;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты - Task Service")
class TaskServiceImplTest {

    @Mock
    private TaskPort taskPort;

    @Mock
    private KafkaProducerPort kafkaProducerPort;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Создание задачи: Успешное создание с валидными данными")
    void createTask_ValidData_Success() {
        TaskDto inputDto = new TaskDto();
        inputDto.setTitle("Valid Title");
        Task entity = new Task();
        when(taskMapper.toEntity(any())).thenReturn(entity);

        taskService.createTask(inputDto);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskPort).save(taskCaptor.capture());
        assertEquals(entity, taskCaptor.getValue());
    }

    @Test
    @DisplayName("Создание задачи: Выставление статуса по умолчанию")
    void createTask_DefaultStatus_SetsNotStarted() {
        TaskDto inputDto = new TaskDto();
        inputDto.setTitle("Title");
        Task entity = new Task();
        when(taskMapper.toEntity(any())).thenReturn(entity);

        taskService.createTask(inputDto);

        assertEquals(TaskStatus.NOT_STARTED, inputDto.getStatus());
    }

    @Test
    @DisplayName("Создание задачи: Ошибка при отсутствии заголовка")
    void createTask_MissingTitle_ThrowsException() {
        TaskDto invalidDto = new TaskDto();

        assertThrows(BadRequestException.class,
                () -> taskService.createTask(invalidDto));
        verify(taskPort, never()).save(any());
    }

    @Test
    @DisplayName("Получение задачи: Успешное получение существующей задачи")
    void getTaskById_ExistingId_ReturnsTask() {
        Long taskId = 1L;
        Task entity = new Task();
        TaskDto expectedDto = new TaskDto();
        when(taskPort.findById(taskId)).thenReturn(Optional.of(entity));
        when(taskMapper.toDto(entity)).thenReturn(expectedDto);

        TaskDto result = taskService.getTaskById(taskId);

        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("Получение задачи: Ошибка при несуществующем ID")
    void getTaskById_NonExistingId_ThrowsException() {
        Long invalidId = 999L;
        when(taskPort.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> taskService.getTaskById(invalidId));
    }

    @Test
    @DisplayName("Обновление задачи: Отправка Kafka сообщения при изменении статуса")
    void updateTask_StatusChanged_SendsKafkaMessage() {
        TaskDto inputDto = new TaskDto();
        inputDto.setId(1L);
        inputDto.setStatus(TaskStatus.IN_PROGRESS);

        Task oldTask = new Task();
        oldTask.setStatus(TaskStatus.NOT_STARTED);
        when(taskPort.findById(1L)).thenReturn(Optional.of(oldTask));
        when(taskMapper.toEntity(any())).thenReturn(new Task());

        taskService.updateTask(inputDto);

        verify(kafkaProducerPort).sendStatusUpdate(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Обновление задачи: Не отправляет Kafka сообщение при одинаковом статусе")
    void updateTask_StatusNotChanged_NoKafkaMessage() {
        TaskDto inputDto = new TaskDto();
        inputDto.setId(1L);
        inputDto.setStatus(TaskStatus.COMPLETED);

        Task oldTask = new Task();
        oldTask.setStatus(TaskStatus.COMPLETED);
        when(taskPort.findById(1L)).thenReturn(Optional.of(oldTask));
        when(taskMapper.toEntity(any())).thenReturn(new Task());

        taskService.updateTask(inputDto);

        verify(kafkaProducerPort, never()).sendStatusUpdate(any(), any());
    }

    @Test
    @DisplayName("Удаление задачи: Успешное удаление существующей задачи")
    void deleteTaskById_ExistingTask_DeletesSuccessfully() {
        Long taskId = 1L;
        Task existingTask = new Task();
        when(taskPort.findById(taskId)).thenReturn(Optional.of(existingTask));

        taskService.deleteTaskById(taskId);

        verify(taskPort).delete(existingTask);
    }

    @Test
    @DisplayName("Удаление задачи: Ошибка при несуществующем ID")
    void deleteTaskById_NonExistingId_ThrowsException() {
        Long invalidId = 999L;
        when(taskPort.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> taskService.deleteTaskById(invalidId));
        verify(taskPort, never()).delete(any());
    }

    @Test
    @DisplayName("Получение всех задач: Возвращает список DTO")
    void getTasks_ReturnsListOfDtos() {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskPort.findAll()).thenReturn(tasks);
        when(taskMapper.toDto(any(Task.class))).thenReturn(new TaskDto());

        List<TaskDto> result = taskService.getTasks();

        assertEquals(2, result.size());
        verify(taskMapper, times(2)).toDto(any(Task.class));
    }
}
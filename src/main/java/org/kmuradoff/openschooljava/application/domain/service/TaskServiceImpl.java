package org.kmuradoff.openschooljava.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.kmuradoff.openschooljava.application.port.in.TaskService;
import org.kmuradoff.openschooljava.application.port.out.KafkaProducerPort;
import org.kmuradoff.openschooljava.application.port.out.TaskPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskPort taskPort;
    private final KafkaProducerPort kafkaProducerPort;

    @Override
    public void createTask(TaskDto taskDto) {
        taskDto.setStatus(TaskStatus.IN_PROGRESS);
        taskPort.createTask(taskDto);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskPort.getTaskById(id);
    }

    @Override
    public void updateTask(TaskDto taskDto) {
        var taskId = taskDto.getId();
        TaskDto existingTask = null;

        if (taskId != null) {
            existingTask = taskPort.getTaskById(taskId);
        }

        if (existingTask == null) {
            return;
        }

        if (taskDto.getStatus() == null || taskDto.getStatus() == existingTask.getStatus()) {
            updateTaskData(taskDto);
        } else {
            kafkaProducerPort.sendStatusUpdate(taskId, existingTask.getStatus());
        }
    }

    @Override
    public TaskDto updateTaskStatus(Long id, TaskStatus taskStatus) {
        var taskDto = taskPort.getTaskById(id);
        taskDto.setStatus(taskStatus);
        return taskPort.updateTask(taskDto);
    }

    @Override
    public void updateTaskData(TaskDto taskDto) {
        taskPort.updateTask(taskDto);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskPort.deleteTaskById(id);
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskPort.getTasks();
    }
}

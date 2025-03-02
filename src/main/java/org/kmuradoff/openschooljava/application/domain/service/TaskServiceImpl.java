package org.kmuradoff.openschooljava.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.adapter.out.postgres.exception.TaskAdapterException;
import org.kmuradoff.openschooljava.adapter.out.postgres.mapper.TaskMapper;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
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
    private final TaskMapper taskMapper;

    @Override
    public void createTask(TaskDto taskDto) {
        taskDto.setStatus(TaskStatus.NOT_STARTED);
        taskPort.save(taskMapper.toEntity(taskDto));
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskPort.findById(id)
                .orElseThrow(() -> new TaskAdapterException("Task not found for id: " + id));

        return taskMapper.toDto(task);
    }

    public void updateTask(TaskDto taskDto) {
        var oldTask = taskPort.findById(taskDto.getId())
                .orElseThrow(() -> new TaskAdapterException("Task not found for id: " + taskDto.getId()));

        var previousStatus = oldTask.getStatus();

        taskPort.save(taskMapper.toEntity(taskDto));

        if(previousStatus != taskDto.getStatus()) {
            kafkaProducerPort.sendStatusUpdate(taskDto.getId(), taskDto.getStatus());
        }
    }

    @Override
    public void deleteTaskById(Long id) {
        taskPort.deleteById(id);
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskPort.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }
}

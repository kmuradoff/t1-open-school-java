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
        taskDto.setStatus(TaskStatus.NOT_STARTED);
        taskPort.createTask(taskDto);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskPort.getTaskById(id);
    }

    @Override
    public void updateTask(TaskDto taskDto) {
        var oldTask = taskPort.getTaskById(taskDto.getId());
        var updatedTask = taskPort.updateTask(taskDto);

        if(oldTask.getStatus() != updatedTask.getStatus()) {
            kafkaProducerPort.sendStatusUpdate(taskDto.getId(), taskDto.getStatus());
        }
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

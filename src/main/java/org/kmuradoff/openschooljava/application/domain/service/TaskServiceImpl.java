package org.kmuradoff.openschooljava.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.kmuradoff.openschooljava.application.port.in.TaskService;
import org.kmuradoff.openschooljava.application.port.out.TaskPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskPort taskPort;
    @Override
    public void createTask(TaskDto taskDto) {
        taskPort.createTask(taskDto);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskPort.getTaskById(id);
    }

    @Override
    public void updateTask(TaskDto taskDto) {
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

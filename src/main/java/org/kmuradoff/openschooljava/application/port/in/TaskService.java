package org.kmuradoff.openschooljava.application.port.in;

import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;

import java.util.List;

public interface TaskService {
    void createTask(TaskDto taskDto);
    TaskDto getTaskById(Long id);
    void updateTask(TaskDto taskDto);
    void deleteTaskById(Long id);
    List<TaskDto> getTasks();
}

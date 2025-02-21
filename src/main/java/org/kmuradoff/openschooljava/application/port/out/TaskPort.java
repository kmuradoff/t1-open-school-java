package org.kmuradoff.openschooljava.application.port.out;

import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;

import java.util.List;

public interface TaskPort {
    void createTask(TaskDto taskDto);
    TaskDto getTaskById(Long id);
    void updateTask(TaskDto taskDto);
    void deleteTaskById(Long id);
    List<TaskDto> getTasks();
}

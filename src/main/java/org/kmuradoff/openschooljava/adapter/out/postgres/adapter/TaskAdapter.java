package org.kmuradoff.openschooljava.adapter.out.postgres.adapter;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.adapter.out.postgres.exception.TaskAdapterException;
import org.kmuradoff.openschooljava.adapter.out.postgres.mapper.TaskMapper;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.TaskEntity;
import org.kmuradoff.openschooljava.adapter.out.postgres.repository.TaskRepository;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.kmuradoff.openschooljava.application.port.out.TaskPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAdapter implements TaskPort {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    @Override
    public void createTask(TaskDto taskDto) {
        if (taskRepository.existsById(taskDto.getId())) {
            throw new TaskAdapterException("Task already exists");
        }
        TaskEntity taskEntity = taskMapper.toEntity(taskDto);
        taskRepository.save(taskEntity);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new TaskAdapterException("Task not found with id: " + id));
    }

    @Override
    public void updateTask(TaskDto taskDto) {
        TaskEntity existingEntity = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new TaskAdapterException("Task not found with id: " + taskDto.getId()));
        taskMapper.updateEntityFromDto(taskDto, existingEntity);
        taskRepository.save(existingEntity);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }
}

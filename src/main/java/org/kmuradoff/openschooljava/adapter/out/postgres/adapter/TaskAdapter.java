package org.kmuradoff.openschooljava.adapter.out.postgres.adapter;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.adapter.out.postgres.exception.TaskAdapterException;
import org.kmuradoff.openschooljava.adapter.out.postgres.mapper.TaskMapper;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
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
    public boolean existsTaskById(Long id) {
        return taskRepository.existsById(id);
    }

    @Override
    public void createTask(TaskDto taskDto) {
        if (taskRepository.existsById(taskDto.getId())) {
            throw new TaskAdapterException("Task already exists");
        }
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new TaskAdapterException("Task not found with id: " + id));
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task existingEntity = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new TaskAdapterException("Task not found with id: " + taskDto.getId()));
        taskMapper.updateEntityFromDto(taskDto, existingEntity);

        return taskMapper.toDto(taskRepository.save(existingEntity));
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

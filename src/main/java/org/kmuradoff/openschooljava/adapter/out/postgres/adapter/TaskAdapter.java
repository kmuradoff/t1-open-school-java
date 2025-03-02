package org.kmuradoff.openschooljava.adapter.out.postgres.adapter;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
import org.kmuradoff.openschooljava.adapter.out.postgres.repository.TaskRepository;
import org.kmuradoff.openschooljava.application.port.out.TaskPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskAdapter implements TaskPort {

    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    /*@Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task existingEntity = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new TaskAdapterException("Task not found with id: " + taskDto.getId()));
        taskMapper.updateEntityFromDto(taskDto, existingEntity);

        return taskMapper.toDto(taskRepository.save(existingEntity));
    }*/

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}

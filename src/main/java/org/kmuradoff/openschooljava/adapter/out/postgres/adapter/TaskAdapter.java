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

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}

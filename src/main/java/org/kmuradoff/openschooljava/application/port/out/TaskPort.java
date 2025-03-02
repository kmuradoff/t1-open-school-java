package org.kmuradoff.openschooljava.application.port.out;

import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
    void deleteById(Long id);
    List<Task> findAll();
}

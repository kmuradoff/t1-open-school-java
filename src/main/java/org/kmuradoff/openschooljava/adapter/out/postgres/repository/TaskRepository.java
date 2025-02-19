package org.kmuradoff.openschooljava.adapter.out.postgres.repository;

import org.kmuradoff.openschooljava.adapter.out.postgres.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}

package org.kmuradoff.openschooljava.adapter.out.postgres.repository;

import org.kmuradoff.openschooljava.adapter.out.postgres.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}

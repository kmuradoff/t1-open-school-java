package org.kmuradoff.openschooljava.application.port.out;


import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;

public interface KafkaProducerPort {
    void sendStatusUpdate(Long id, TaskStatus status);
}

package org.kmuradoff.openschooljava.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.port.out.KafkaProducerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService implements KafkaProducerPort {
    private final KafkaTemplate<Long, TaskStatus> kafkaTemplate;

    @Override
    public void sendStatusUpdate(Long id, TaskStatus status) {
        kafkaTemplate.send("task-status", id, status);
        log.info("Update task with id {} to status {} event sent via Kafka", id, status);
    }
}

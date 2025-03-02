package org.kmuradoff.openschooljava.adapter.in.kafka;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;
import org.kmuradoff.openschooljava.application.port.in.NotificationService;
import org.kmuradoff.openschooljava.application.port.in.TaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Transactional
    @KafkaListener(topics = "task-status", groupId = "task-id")
    public void handleStatusUpdate(ConsumerRecord<Long, TaskStatus> record, Acknowledgment ack) {
        var id = record.key();
        var status = record.value();

        try {
            log.info("Received message from Kafka: {}", record);
            var task = taskService.updateTaskStatus(id, status);
            log.info("Updated task status to {} for id {}", status, id);

            var notification = EmailNotificationDto.builder()
                    .taskName(task.getTitle())
                    .status(task.getTaskStatus().toString())
                    .build();

            notificationService.sendEmailNotification(notification);
        }
        finally {
            ack.acknowledge();
        }
    }
}

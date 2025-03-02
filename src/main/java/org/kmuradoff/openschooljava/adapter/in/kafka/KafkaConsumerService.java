package org.kmuradoff.openschooljava.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;
import org.kmuradoff.openschooljava.application.port.in.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final NotificationService notificationService;

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            kafkaTemplate = "kafkaTemplate"
    )
    @KafkaListener(topics = "task-status", groupId = "task-id")
    public void handleStatusUpdate(ConsumerRecord<String, TaskStatus> record, Acknowledgment ack) {
        var id = record.key();
        var status = record.value();

        try {
            log.info("Received message from Kafka: {}", record);

            var notification = EmailNotificationDto.builder()
                    .taskId(id)
                    .taskStatus(status.toString())
                    .build();

            notificationService.sendEmailNotification(notification);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while sending email. Will retry.", e);
            throw e;
        }
    }
}
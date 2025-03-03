package org.kmuradoff.openschooljava.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;
import org.kmuradoff.openschooljava.application.port.in.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "task-status", groupId = "task-id")
    public void handleStatusUpdate(List<ConsumerRecord<String, TaskStatus>> records, Acknowledgment ack) {
        log.info("Received {} messages from Kafka", records.size());
        try {
            for (ConsumerRecord<String, TaskStatus> record : records) {
                String id = record.key();
                TaskStatus status = record.value();
                EmailNotificationDto notification = EmailNotificationDto.builder()
                        .taskId(id)
                        .taskStatus(status.toString())
                        .build();
                notificationService.sendEmailNotification(notification);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while sending email notifications. Will retry entire batch.", e);
            throw e;
        }
    }
}
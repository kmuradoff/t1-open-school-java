package org.kmuradoff.openschooljava.adapter.out.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kmuradoff.openschooljava.CommonContainers;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;
import org.kmuradoff.openschooljava.application.port.out.KafkaProducerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@DisplayName("Kafka")
class KafkaProducerServiceTest extends CommonContainers {

    @Autowired
    private KafkaProducerPort producer;

    @Autowired
    private KafkaTemplate<String, TaskStatus> kafkaTemplate;

    @Test
    @DisplayName("Correct: Send Status update")
    void sendStatusUpdate_SendsCorrectMessage() {
        Long taskId = 123L;
        TaskStatus status = TaskStatus.COMPLETED;
        String topic = "task-status";

        Consumer<Object, Object> consumer = createTestConsumer(topic);

        producer.sendStatusUpdate(taskId, status);

        ConsumerRecords<Object, Object> records = KafkaTestUtils.getRecords(consumer);
        assertFalse(records.isEmpty());

        ConsumerRecord<Object, Object> record = records.iterator().next();
        assertEquals(taskId.toString(), record.key());
        assertEquals(status, record.value());
        assertEquals(topic, record.topic());
    }

    private Consumer<Object, Object> createTestConsumer(String topic) {
        Map<String, Object> configs = new HashMap<>(
                kafkaTemplate.getProducerFactory().getConfigurationProperties()
        );

        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");

        ConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(configs);
        Consumer<Object, Object> consumer = factory.createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }
}
package com.wine.to.up.notification.service.messaging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserKafkaConsumerTest {

    private final String TOPIC_NAME = "wine-response-topic";

    private Producer<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private UserServiceKafkaMessageHandler userServiceKafkaMessageHandler;

    @Captor
    ArgumentCaptor<WinePriceUpdatedResponse> winePriceUpdatedResponseArgumentCaptor;

    @Captor
    ArgumentCaptor<String> topicArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> partitionArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> offsetArgumentCaptor;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

    @Test
    void testHandleKafkaMessage() throws JsonProcessingException {
        // Write a message to Kafka using a test producer
        String wineId = "11111";
        String message = objectMapper.writeValueAsString(new WinePriceUpdatedResponse(wineId, "testWine", (float) 100, null));
        producer.send(new ProducerRecord<>(TOPIC_NAME, 0, wineId , message));
        producer.flush();

        // Read the message and assert its properties
        verify(userServiceKafkaMessageHandler, timeout(5000).times(1))
                .handle(KafkaMessageSentEventOuterClass.KafkaMessageSentEvent.getDefaultInstance());

        WinePriceUpdatedResponse wineResponse = winePriceUpdatedResponseArgumentCaptor.getValue();
        assertNotNull(wineResponse);
        assertEquals("11111", wineResponse.getWineId());
        assertEquals("testWine", wineResponse.getWineName());
        assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue());
    }
}
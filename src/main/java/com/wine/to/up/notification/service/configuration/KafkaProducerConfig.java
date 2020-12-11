package com.wine.to.up.notification.service.configuration;

import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-server}")
    private String kafkaServer;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<Long, WinePriceUpdatedWithTokensEvent>producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, WinePriceUpdatedWithTokensEvent> kafkaTemplate() {
        KafkaTemplate<Long,WinePriceUpdatedWithTokensEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        kafkaTemplate.setDefaultTopic("wine-response-topic");
        return kafkaTemplate;
    }

}

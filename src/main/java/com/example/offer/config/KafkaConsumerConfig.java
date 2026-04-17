package com.example.offer.config;

import com.example.offer.dto.PaymentTransaction; // 1. Use the Record
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties; // For Manual Ack
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.core.task.VirtualThreadTaskExecutor; // Java 21 Flex
import com.fasterxml.jackson.databind.ObjectMapper; // Standard for SB3
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;



import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, PaymentTransaction> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-settlement-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JacksonJsonDeserializer<PaymentTransaction> deserializer =
                new JacksonJsonDeserializer<>(PaymentTransaction.class);

        deserializer.addTrustedPackages("com.example.offer.model");

        var factory = new DefaultKafkaConsumerFactory<String, PaymentTransaction>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        factory.setValueDeserializer(deserializer);

        return factory;
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentTransaction> kafkaListenerContainerFactory(
            ConsumerFactory<String, PaymentTransaction> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, PaymentTransaction> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // 3. THE BANKING GUARD: Manual Acknowledgement
        // We only ACK after the Postgres transaction is committed.
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // 4. THE JAVA 21 FLEX: Virtual Thread Executor
        factory.getContainerProperties().setListenerTaskExecutor(new VirtualThreadTaskExecutor());

        return factory;
    }
}

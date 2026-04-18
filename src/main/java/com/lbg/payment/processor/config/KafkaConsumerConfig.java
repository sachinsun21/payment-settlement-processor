package com.lbg.payment.processor.config;

//import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

/*@Configuration
@EnableKafka*/
public class KafkaConsumerConfig {
/*
    @Bean
    public ConsumerFactory<String, PaymentEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-settlement-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JacksonJsonDeserializer<PaymentEvent> deserializer =
                new JacksonJsonDeserializer<>(PaymentEvent.class);

        deserializer.addTrustedPackages("com.example.offer.model");

        var factory = new DefaultKafkaConsumerFactory<String, PaymentEvent>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        factory.setValueDeserializer(deserializer);

        return factory;
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, PaymentEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // 3. THE BANKING GUARD: Manual Acknowledgement
        // We only ACK after the Postgres transaction is committed.
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // 4. THE JAVA 21 FLEX: Virtual Thread Executor
        factory.getContainerProperties().setListenerTaskExecutor(new VirtualThreadTaskExecutor());

        return factory;
    }*/
}

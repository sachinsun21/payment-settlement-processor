package com.example.offer.config;
import com.example.offer.dto.OfferDTO;
import com.example.offer.dto.PaymentTransaction;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.json.JsonMapper;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

 @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

 @Bean
 public ProducerFactory<String, PaymentTransaction> producerFactory(JsonMapper jsonMapper){
     Map<String,Object> configProps=new HashMap<>();
     configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
     configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
     //configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
     return new DefaultKafkaProducerFactory<>(configProps,
     new StringSerializer(), new JacksonJsonSerializer<>(jsonMapper)
     );
 }
@Bean
    public KafkaTemplate<String, PaymentTransaction> kafkaTemplate(ProducerFactory<String, PaymentTransaction> producerFactory){
     return new KafkaTemplate<>(producerFactory);
}
}

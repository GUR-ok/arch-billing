package ru.gur.archbilling.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"hw09","local"})
public class KafkaProducerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String SERVER;

    private ProducerFactory<String, Double> producerFactoryDouble() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                SERVER);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                DoubleSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private ProducerFactory<String, String> producerFactoryString() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                SERVER);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Double> kafkaTemplateDouble() {
        return new KafkaTemplate<>(producerFactoryDouble());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateString() {
        return new KafkaTemplate<>(producerFactoryString());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("order", 2, (short) 1);
    }

    @Bean
    public NewTopic billingTopic() {
        return new NewTopic("billing", 2, (short) 1);
    }

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payment", 2, (short) 1);
    }

    @Bean
    public NewTopic topicAccountsWithBalance() {
        return new NewTopic("AccountsWithBalance", 3, (short) 1);
    }

    @Bean
    public NewTopic topicKeyValueTopic() {
        return new NewTopic("KeyValueTopic", 3, (short) 1);
    }
}

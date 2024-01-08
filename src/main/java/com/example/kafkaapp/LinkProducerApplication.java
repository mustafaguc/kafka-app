package com.example.kafkaapp;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class LinkProducerApplication {

    @Bean
    public KafkaAdmin admin(@Value("${spring.kafka.bootstrap-servers}") String kafkaBrokers,
                            @Value("${spring.kafka.properties.sasl.jaas.config}") String jaasConfig) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        configs.put(AdminClientConfig.DEFAULT_SECURITY_PROTOCOL, "PLAINTEXT");
        configs.put(SaslConfigs.SASL_MECHANISM, "GSSAPI");
        configs.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new KafkaAdmin(configs);
    }

    public static void main(String[] args) {
        SpringApplication.run(LinkProducerApplication.class, args);
    }

}

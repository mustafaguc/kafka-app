package com.example.kafkaapp;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.LongStream;

@RestController
public class DefaultRestController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int partitionCount = 10;

    public DefaultRestController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/send/{message}/{count}")
    public void sendMessage(@PathVariable String message, @PathVariable Optional<Integer> count) {
        LongStream.range(0, count.orElse(100))
                .forEach(num -> produceMessage(num, message));
    }

    private void produceMessage(Long num, String message) {
        kafkaTemplate.send("test1", num.intValue() % partitionCount, "key", message + " : " + num);
    }

}

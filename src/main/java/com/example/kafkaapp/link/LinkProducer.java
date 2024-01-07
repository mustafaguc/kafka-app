package com.example.kafkaapp.link;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class LinkProducer {

    private final static int PARTITION_COUNT = 8;
    private final static String TOPIC = "LINKS";
    private final static short REPLICATION_FACTOR = 1;
    private final KafkaTemplate<String, Link> kafkaTemplate;

    public LinkProducer(KafkaTemplate<String, Link> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    public void sendLinkMessages(List<Link> links) {
        IntStream.range(0, links.size())
                .forEach(index -> sendLinkMessage(index, links.get(index)));
    }

    private void sendLinkMessage(int index, Link link) {
        kafkaTemplate.send(TOPIC, index % PARTITION_COUNT, "key", link);
    }
}

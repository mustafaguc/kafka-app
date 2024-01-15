package com.example.kafkaapp.link;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

/**
 * The LinkProducer class is responsible for producing Link messages to a Kafka topic.
 * It configures the topic, partitions, and replication factor and provides methods for sending Link messages.
 */
@Component
public class LinkProducer {

    // Constants for topic configuration
    private final static int PARTITION_COUNT = 80;
    private final static String TOPIC = "LINKS";
    private final static short REPLICATION_FACTOR = 1;

    // KafkaTemplate for sending Link messages
    private final KafkaTemplate<String, Link> kafkaTemplate;

    /**
     * Constructor for the LinkProducer class.
     *
     * @param kafkaTemplate The KafkaTemplate used for sending Link messages.
     */
    public LinkProducer(KafkaTemplate<String, Link> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Configures the Kafka topic with the specified partition count and replication factor.
     *
     * @param kafkaAdmin The KafkaAdmin instance used for topic configuration.
     */
    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    /**
     * Sends a list of Link messages to the configured Kafka topic.
     *
     * @param links The list of Link objects to be sent.
     */
    public void sendLinkMessages(List<Link> links) {
        IntStream.range(0, links.size())
                .forEach(index -> sendLinkMessage(links.get(index)));
    }

    /**
     * Sends an individual Link message to the configured Kafka topic.
     *
     * @param index The index of the Link message in the list.
     * @param link  The Link object to be sent.
     */
    private void sendLinkMessage(int index, Link link) {
        // Sends the Link message to the topic, distributing across partitions based on the index
        kafkaTemplate.send(TOPIC, index % PARTITION_COUNT, "key", link);
    }

    /**
     * Sends an individual Link message to the configured Kafka topic.
     *
     * @param link The Link object to be sent.
     */
    private void sendLinkMessage(Link link) {
        // Sends the Link message to the topic, distributing across partitions based on the hash code of the link
        kafkaTemplate.send(TOPIC, "KEY-" + (link.href().hashCode() % PARTITION_COUNT), link);
    }
}
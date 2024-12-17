package co.istad.reporting.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.user-verified-events-topic}")
    private String topicUserVerifiedEvent;

    @Value("${kafka.topic.user-registered-events-topic}")
    private String topicUserRegisteredEvent;

    @Bean
    public NewTopic userRegisteredEventsTopic() {
        return TopicBuilder
                .name(topicUserRegisteredEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userVerifiedEventsTopic() {
        return TopicBuilder
                .name(topicUserVerifiedEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }

}

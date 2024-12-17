package co.istad.reporting.producer;

import co.istad.core.event.UserRegisteredEvent;
import co.istad.reporting.domain.primary.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @Value("${kafka.topic.user-registered-events-topic}")
    private String topicUserRegisteredEvents;

    @Value("${kafka.topic.user-verified-events-topic}")
    private String topicUserVerifiedEvents;


    public void produceUserRegisteredEvent(EmailVerification emailVerification) {
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent();
        userRegisteredEvent.setEmail(emailVerification.getUser().getEmail());
        userRegisteredEvent.setUsername(emailVerification.getUser().getUsername());
        userRegisteredEvent.setToken(emailVerification.getToken());
        kafkaTemplate.send(topicUserRegisteredEvents, userRegisteredEvent);
    }


    public void produceEmailVerifiedEvent(EmailVerification emailVerification) {
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent();
        userRegisteredEvent.setEmail(emailVerification.getUser().getEmail());
        userRegisteredEvent.setUsername(emailVerification.getUser().getUsername());
        userRegisteredEvent.setToken(emailVerification.getToken());
        kafkaTemplate.send(topicUserVerifiedEvents, userRegisteredEvent);
    }


}

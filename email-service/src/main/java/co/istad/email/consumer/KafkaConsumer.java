package co.istad.email.consumer;

import co.istad.core.event.UserRegisteredEvent;
import co.istad.email.MailRequest;
import co.istad.email.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "${kafka.topic.user-registered-events-topic}", groupId = "${spring.application.name}")
    public void listenUserRegisteredEvents(UserRegisteredEvent event) {
        System.out.println("Received user registered event: " + event.getEmail());
        System.out.println("Received user registered event: " + event.getToken());
        // Send Mail
        MailRequest<?> mailRequest = MailRequest
                .builder()
                .to(event.getEmail())
                .subject("Account Verification")
                .template("verify/account-verify")
                .data(event.getToken())
                .build();

        mailService.send(mailRequest);
    }

    @KafkaListener(topics = "${kafka.topic.user-verified-events-topic}", groupId = "${spring.application.name}")
    public void listenUserVerifiedEvents(UserRegisteredEvent event) {
        // Send Mail
        MailRequest<?> mailRequest = MailRequest
                .builder()
                .to(event.getEmail())
                .subject("Account Verification")
                .template("verify/account-verify-success")
                .data(event.getUsername())
                .build();

        mailService.send(mailRequest);
    }

}

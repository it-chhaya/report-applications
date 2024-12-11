package co.istad.email.config;

import co.istad.email.MailRequest;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class MailServerConfig {

    private final JavaMailSender javaMailSender;
    private Context context;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String adminMail;

    public void send(MailRequest<?> mailRequest) {
        // TODO

        context = new Context();
        context.setVariable("data", mailRequest.data());

        String proceedTemplate = templateEngine.process(mailRequest.template(), context);

        // 1. Create Message Object
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // 2. Set Message Object Information
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(proceedTemplate, true);
            mimeMessageHelper.setTo(mailRequest.to());
            mimeMessageHelper.setFrom(adminMail);
            mimeMessageHelper.setCc(mailRequest.cc());
            mimeMessageHelper.setSubject(mailRequest.subject());

            Resource resource = new ClassPathResource("orders.csv");
            mimeMessageHelper.addAttachment("report", resource.getFile());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error send mail: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error attachment: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}

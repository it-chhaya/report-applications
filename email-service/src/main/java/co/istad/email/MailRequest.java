package co.istad.email;

import lombok.Builder;

@Builder
public record MailRequest<T>(
        String to,
        String cc,
        String subject,
        String template,
        T data
) {
}

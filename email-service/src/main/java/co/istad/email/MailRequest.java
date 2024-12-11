package co.istad.email;

public record MailRequest<T>(
        String to,
        String cc,
        String subject,
        String template,
        T data
) {
}

package co.istad.email;

public record MailResponse(
        String to,
        String cc,
        String subject
) {
}

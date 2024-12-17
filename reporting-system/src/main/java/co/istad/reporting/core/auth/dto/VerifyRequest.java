package co.istad.reporting.core.auth.dto;

public record VerifyRequest(
        String email,
        String token
) {
}

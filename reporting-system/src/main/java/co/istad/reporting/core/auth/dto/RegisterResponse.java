package co.istad.reporting.core.auth.dto;

public record RegisterResponse(
        String email,
        String familyName,
        String givenName,
        Boolean emailVerified
) {
}

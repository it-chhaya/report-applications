package co.istad.reporting.core.auth.dto;

public record RegisterRequest(
        String familyName,
        String givenName,
        String gender,
        String username,
        String email,
        String password,
        String confirmedPassword
) {
}

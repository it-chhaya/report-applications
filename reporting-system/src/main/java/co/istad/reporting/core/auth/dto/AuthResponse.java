package co.istad.reporting.core.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}

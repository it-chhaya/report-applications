package co.istad.core.dto;

public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {
}

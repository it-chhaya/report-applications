package co.istad.reporting.core.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}

package co.istad.reporting.core.auth;

import co.istad.reporting.core.auth.dto.AuthResponse;
import co.istad.reporting.core.auth.dto.RefreshTokenRequest;

public interface AuthService {

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    AuthResponse login(String username, String password);
}

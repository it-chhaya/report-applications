package co.istad.reporting.core.auth;

import co.istad.reporting.core.auth.dto.AuthResponse;
import co.istad.reporting.core.auth.dto.RefreshTokenRequest;
import co.istad.reporting.core.auth.dto.RegisterRequest;
import co.istad.reporting.core.auth.dto.RegisterResponse;

public interface AuthService {

    void verify(String email, String token);

    RegisterResponse register(RegisterRequest registerRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    AuthResponse login(String username, String password);
}

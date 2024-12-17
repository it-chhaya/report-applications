package co.istad.reporting.core.auth;

import co.istad.reporting.core.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify")
    void verify(@RequestBody VerifyRequest verifyRequest) {
        authService.verify(verifyRequest.email(), verifyRequest.token());
    }


    @PostMapping("/register")
    RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }


    @PostMapping("/refresh-token")
    AuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }


    @PostMapping("/login")
    AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.username(),
                loginRequest.password());
    }

}

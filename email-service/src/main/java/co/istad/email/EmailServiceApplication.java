package co.istad.email;

import co.istad.core.dto.AuthResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailServiceApplication.class, args);

        AuthResponse authResponse = new AuthResponse(
                "Bearer",
                "Access Token",
                "Refresh Token"
        );

    }

}

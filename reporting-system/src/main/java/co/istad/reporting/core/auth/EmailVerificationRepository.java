package co.istad.reporting.core.auth;

import co.istad.reporting.domain.primary.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends
        JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByUserEmail(String userEmail);

}

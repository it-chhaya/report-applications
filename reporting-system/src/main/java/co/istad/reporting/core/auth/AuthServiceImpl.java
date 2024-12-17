package co.istad.reporting.core.auth;

import co.istad.reporting.core.auth.dto.AuthResponse;
import co.istad.reporting.core.auth.dto.RefreshTokenRequest;
import co.istad.reporting.core.auth.dto.RegisterRequest;
import co.istad.reporting.core.auth.dto.RegisterResponse;
import co.istad.reporting.core.user.UserMapper;
import co.istad.reporting.core.user.UserRepository;
import co.istad.reporting.domain.primary.EmailVerification;
import co.istad.reporting.domain.primary.User;
import co.istad.reporting.producer.KafkaProducer;
import co.istad.reporting.utils.TokenGenerationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtAuthenticationProvider jwtAuth;
    private final DaoAuthenticationProvider daoAuth;
    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtEncoder refreshTokenJwtEncoder;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final EmailVerificationRepository emailVerificationRepository;

    private final KafkaProducer kafkaProducer;


    @Override
    public void verify(String email, String token) {

        // Validate email
        EmailVerification emailVerification = emailVerificationRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        // Validate token
        if (!emailVerification.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // Validate token expiration
        if (Instant.now().isAfter(emailVerification.getExpiration())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired token");
        }

        User user = emailVerification.getUser();
        user.setEmailVerified(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setIsEnabled(true);

        userRepository.save(user);
        emailVerificationRepository.delete(emailVerification);

        // Publish kafka message
        kafkaProducer.produceEmailVerifiedEvent(emailVerification);
    }


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Validate username
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        // Validate email
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        // Validate password and confirmed password
        if (!registerRequest.password()
                .equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords do not match"
            );
        }

        // Prepare data
        User user = userMapper.fromRegisterRequest(registerRequest);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        user.setIsEnabled(false);
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);

        Instant now = Instant.now();

        // Email Verification Process
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setToken(TokenGenerationUtil.generateSecureDigits());
        emailVerification.setExpiration(now.plus(1, ChronoUnit.MINUTES));
        emailVerification.setUser(user);

        emailVerification = emailVerificationRepository.save(emailVerification);

        // Kafka publish event (UserRegisteredEvent)
        kafkaProducer.produceUserRegisteredEvent(emailVerification);

        return userMapper.toRegisterResponse(user);
    }


    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        Authentication auth =
                new BearerTokenAuthenticationToken(
                        refreshTokenRequest.refreshToken()
                );
        auth = jwtAuth.authenticate(auth);

        log.info("Auth: {}", auth.getPrincipal());
        log.info("Authorities: {}", auth.getAuthorities());
        // Generate SCOPES
        // user:write user:read
        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Jwt jwt = (Jwt) auth.getPrincipal();

        log.info("Refresh token: {}", jwt.getId());
        log.info("Refresh token: {}", jwt.getTokenValue());

        Instant now = Instant.now();

        // Create JWT Token
        JwtClaimsSet accessTokenClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access APIs")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters accessTokenParameters =
                JwtEncoderParameters.from(accessTokenClaimsSet);

        String accessToken = accessTokenJwtEncoder
                .encode(accessTokenParameters)
                .getTokenValue();

        return new AuthResponse(accessToken, refreshTokenRequest.refreshToken());
    }


    @Override
    public AuthResponse login(String username, String password) {

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        username, password
                );
        auth = daoAuth.authenticate(auth);

        // Generate SCOPES
        // user:write user:read
        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        log.info("Scope: {}", scope);

        Instant now = Instant.now();

        // Create JWT Token
        JwtClaimsSet accessTokenClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access APIs")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters accessTokenParameters =
                JwtEncoderParameters.from(accessTokenClaimsSet);

        String accessToken = accessTokenJwtEncoder
                .encode(accessTokenParameters)
                .getTokenValue();

        JwtClaimsSet refreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Refresh Token")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters refreshTokenParameters =
                JwtEncoderParameters.from(refreshTokenClaimsSet);

        String refreshToken = refreshTokenJwtEncoder
                .encode(refreshTokenParameters)
                .getTokenValue();

        return new AuthResponse(accessToken, refreshToken);
    }

}

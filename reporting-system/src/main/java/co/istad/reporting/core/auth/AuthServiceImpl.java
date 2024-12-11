package co.istad.reporting.core.auth;

import co.istad.reporting.core.auth.dto.AuthResponse;
import co.istad.reporting.core.auth.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

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

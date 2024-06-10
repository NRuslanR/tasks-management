package edu.examples.todos.presentation.api.security.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
@Validated
public class JwtServiceImpl implements JwtService
{
    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    public JwtServiceImpl(@Valid JwtProperties jwtProperties)
    {
        this.jwtProperties = jwtProperties;

        secretKey =
                StringUtils.hasText(jwtProperties.getSecretKey()) ?
                        Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey())) :
                        Jwts.SIG.HS256.key().random(new SecureRandom()).build();
    }

    @Override
    public String createJwtTokenString(@Valid BaseJwtTokenInfo baseJwtTokenInfo) throws NullPointerException, JwtServiceException
    {
        var currentDateTime = LocalDateTime.now();
        var currentZoneOffset = OffsetDateTime.now().getOffset();

        return
                Jwts
                    .builder()
                    .issuedAt(Date.from(currentDateTime.toInstant(currentZoneOffset)))
                    .expiration(Date.from(currentDateTime.plusSeconds(jwtProperties.getExpiryTimeSec()).toInstant(currentZoneOffset)))
                    .subject(baseJwtTokenInfo.getSubject())
                    .issuer(jwtProperties.getIssuer())
                    .claims(baseJwtTokenInfo.getClaims())
                    .signWith(secretKey)
                    .compact();
    }

    @Override
    public JwtTokenInfo validateJwtTokenString(@NotBlank String jwtTokenString) throws NullPointerException, JwtServiceException
    {
        var jwtParser =
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .build();

        var claims = jwtParser.parseClaimsJws(jwtTokenString);

        ensureClaimsValid(claims);

        var payload = claims.getPayload();

        return JwtTokenInfo.of(
                BaseJwtTokenInfo.of(
                        payload.getSubject(),
                        payload
                ),
                LocalDateTime.ofInstant(payload.getIssuedAt().toInstant(), ZoneId.systemDefault()),
                payload.getIssuer(),
                LocalDateTime.ofInstant(payload.getExpiration().toInstant(), ZoneId.systemDefault())
        );
    }

    private void ensureClaimsValid(Jws<Claims> claims) throws NullPointerException, JwtServiceException
    {
        var payload = claims.getPayload();
        var currentZoneOffset = OffsetDateTime.now().getOffset();

        if (!payload.getIssuer().equals(jwtProperties.getIssuer()))
            throw new JwtServiceException("Unknown token issuer");

        if (payload.getExpiration().toInstant().isBefore(LocalDateTime.now().toInstant(currentZoneOffset)))
            throw new JwtServiceException("Token is expired");
    }
}

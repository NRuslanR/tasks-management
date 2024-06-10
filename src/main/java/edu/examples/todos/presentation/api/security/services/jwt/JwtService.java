package edu.examples.todos.presentation.api.security.services.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public interface JwtService
{
    String createJwtTokenString(@Valid BaseJwtTokenInfo baseJwtTokenInfo) throws NullPointerException, JwtServiceException;
    JwtTokenInfo validateJwtTokenString(@NotBlank String jwtTokenString) throws NullPointerException, JwtServiceException ;
}

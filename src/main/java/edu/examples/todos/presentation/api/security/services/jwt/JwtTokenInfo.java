package edu.examples.todos.presentation.api.security.services.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class JwtTokenInfo
{
    @Delegate
    private BaseJwtTokenInfo baseJwtTokenInfo;

    private LocalDateTime issuedAt;
    private String issuer;
    private LocalDateTime expiredAt;
}

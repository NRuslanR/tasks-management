package edu.examples.todos.presentation.api.security.services.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "application.security.authentication.methods.jwt")
public class JwtProperties
{
    @NotBlank
    private String issuer;

    private String secretKey;

    @Min(30)
    private long ExpiryTimeSec;
}

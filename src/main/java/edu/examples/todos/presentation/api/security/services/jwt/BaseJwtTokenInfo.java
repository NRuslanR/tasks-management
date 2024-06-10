package edu.examples.todos.presentation.api.security.services.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class BaseJwtTokenInfo
{
    @NotBlank
    private String subject;

    private Map<String, Object> claims;
}

package edu.examples.todos.features.clients.sign_in;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignInRequest
{
    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;
}

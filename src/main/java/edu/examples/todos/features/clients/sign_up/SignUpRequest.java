package edu.examples.todos.features.clients.sign_up;

import edu.examples.todos.presentation.api.security.services.clients.ClientId;
import edu.examples.todos.presentation.api.security.services.clients.ClientSecret;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignUpRequest
{
    @ClientId
    private String clientId;

    @ClientSecret
    private String clientSecret;

    private String firstName;
    private String lastName;
}

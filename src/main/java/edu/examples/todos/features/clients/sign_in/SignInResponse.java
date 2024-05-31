package edu.examples.todos.features.clients.sign_in;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignInResponse extends RepresentationModel<SignInResponse>
{
    private ClientInfoResource clientInfoResource;
}

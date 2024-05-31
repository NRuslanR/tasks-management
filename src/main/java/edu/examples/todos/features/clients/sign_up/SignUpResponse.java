package edu.examples.todos.features.clients.sign_up;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.examples.todos.features.clients.shared.ClientInfoResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignUpResponse extends RepresentationModel<SignUpResponse>
{
    @JsonUnwrapped
    private ClientInfoResource clientInfoResource;
}

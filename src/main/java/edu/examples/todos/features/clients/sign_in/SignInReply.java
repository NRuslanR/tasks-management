package edu.examples.todos.features.clients.sign_in;

import edu.examples.todos.features.clients.shared.ClientInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignInReply
{
    private ClientInfo clientInfo;
}

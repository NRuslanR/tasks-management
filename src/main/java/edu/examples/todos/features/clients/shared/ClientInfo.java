package edu.examples.todos.features.clients.shared;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.examples.todos.usecases.users.accounting.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ClientInfo
{
    private String clientId;

    private Collection<String> clientAuthorities;

    @JsonUnwrapped
    private UserDto user;
}

package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.features.clients.shared.ClientInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GetClientInfoResult
{
    private ClientInfo clientInfo;
}

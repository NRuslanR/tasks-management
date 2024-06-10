package edu.examples.todos.features.clients.get_client_info;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GetClientInfoQuery
{
    @NotBlank
    private String clientId;
}

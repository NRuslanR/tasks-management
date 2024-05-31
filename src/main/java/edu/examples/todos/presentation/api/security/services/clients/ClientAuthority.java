package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
@Entity
public class ClientAuthority
{
    @Id
    @NonNull
    private String name;
}

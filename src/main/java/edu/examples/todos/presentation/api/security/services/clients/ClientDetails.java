package edu.examples.todos.presentation.api.security.services.clients;

import edu.examples.todos.domain.resources.users.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
@Entity
public class ClientDetails
{
    @Id
    @NotBlank
    private String id;

    @NotBlank
    private String secret;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(
                    name = "value",
                    column = @Column(name = "userId")
            )
    )
    private UserId userId;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "CLIENTS__AUTHORITIES",
            joinColumns = {
                    @JoinColumn(name = "client_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "authority_id", referencedColumnName = "name")
            }
    )
    private Set<ClientAuthority> authorities;

    public Collection<String> getAuthorityNames()
    {
        return
                Optional
                        .ofNullable(authorities).map(v -> v.stream().map(ClientAuthority::getName).toList())
                        .orElseGet(ArrayList::new);
    }
}

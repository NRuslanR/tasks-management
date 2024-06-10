package edu.examples.todos.presentation.api.security.services.clients;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ClientUserDetails extends User
{
    private String toDoUserId;

    public ClientUserDetails(String username, String password, String toDoUserId, Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);

        setToDoUserId(toDoUserId);
    }

    public ClientUserDetails(
            String username, String password, String toDoUserId, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities
    )
    {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        setToDoUserId(toDoUserId);
    }

    public String getToDoUserId()
    {
        return toDoUserId;
    }

    public void setToDoUserId(String value)
    {
        toDoUserId = value;
    }
}

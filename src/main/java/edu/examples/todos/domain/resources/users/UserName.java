package edu.examples.todos.domain.resources.users;

import jakarta.persistence.Embeddable;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
@Embeddable
public class UserName
{
    String firstName;
    String lastName;

    protected UserName()
    {
        firstName = "";
        lastName = "";
    }

    public UserName changeFirstName(String value)
    {
        return UserName.of(value, lastName);
    }

    public UserName changeLastName(String value)
    {
        return UserName.of(firstName, value);
    }
}

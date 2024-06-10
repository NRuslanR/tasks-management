package edu.examples.todos.domain.resources.users;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.springframework.util.StringUtils;

@Value
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

    public static UserName of(String firstName, String lastName)
    {
        return new UserName(firstName, lastName);
    }

    private UserName(String firstName, String lastName)
    {
        if (!StringUtils.hasText(firstName))
            throw new IncorrectUserNameException("User's first name must be assigned");

        if (!StringUtils.hasText(lastName))
            throw new IncorrectUserNameException("User's last name must be assigned");

        this.firstName = firstName;
        this.lastName = lastName;
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

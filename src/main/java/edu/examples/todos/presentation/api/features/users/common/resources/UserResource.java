package edu.examples.todos.presentation.api.features.users.common.resources;

import edu.examples.todos.usecases.users.accounting.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

public class UserResource extends EntityModel<UserDto>
{
    public UserResource(UserDto userDto, Link... links)
    {
        super(userDto, List.of(links));
    }
}

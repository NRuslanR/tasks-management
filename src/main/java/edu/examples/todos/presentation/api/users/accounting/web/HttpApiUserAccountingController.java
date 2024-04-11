package edu.examples.todos.presentation.api.users.accounting.web;

import edu.examples.todos.presentation.api.users.accounting.AbstractApiUserAccountingController;
import edu.examples.todos.presentation.api.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.users.common.resources.UserResourceAssembler;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class HttpApiUserAccountingController extends AbstractApiUserAccountingController
{
    public HttpApiUserAccountingController(
            UserAccountingCommandUseCases userAccountingCommandUseCases,
            UserResourceAssembler userResourceAssembler
    )
    {
        super(userAccountingCommandUseCases, userResourceAssembler);
    }

    @Override
    @PostMapping
    public Mono<UserResource> createUser(@RequestBody CreateUserCommand command)
    {
        return super.createUser(command);
    }
}

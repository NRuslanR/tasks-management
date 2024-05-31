package edu.examples.todos.presentation.api.features.users.accounting.web;

import edu.examples.todos.presentation.api.features.users.accounting.AbstractApiUserAccountingController;
import edu.examples.todos.presentation.api.features.users.common.resources.UserResource;
import edu.examples.todos.presentation.api.features.users.common.resources.UserResourceAssembler;
import edu.examples.todos.usecases.users.accounting.commands.UserAccountingCommandUseCases;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import edu.examples.todos.usecases.users.accounting.queries.UserAccountingQueryUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class HttpApiUserAccountingController extends AbstractApiUserAccountingController
{
    public HttpApiUserAccountingController(
            UserAccountingQueryUseCases userAccountingQueryUseCases,
            UserAccountingCommandUseCases userAccountingCommandUseCases,
            UserResourceAssembler userResourceAssembler
    )
    {
        super(userAccountingQueryUseCases, userAccountingCommandUseCases, userResourceAssembler);
    }

    @Override
    @GetMapping(path = "/{userId}")
    public Mono<UserResource> getUserById(@PathVariable("userId") String userId)
    {
        return super.getUserById(userId);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResource> createUser(@RequestBody CreateUserCommand command)
    {
        return super.createUser(command);
    }

    @Override
    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeUser(@PathVariable("userId") String userId)
    {
        return super.removeUser(userId);
    }
}

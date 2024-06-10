package edu.examples.todos.usecases.users.accounting.services;

import edu.examples.todos.domain.resources.users.UserId;

public interface CurrentUserService
{
    UserId getCurrentUserId();
}

package edu.examples.todos.usecases.users.accounting.services;

import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsService;
import edu.examples.todos.presentation.api.security.services.clients.ClientUserDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@ConditionalOnBean(ClientDetailsService.class)
@Service
public class CurrentUserServiceImpl implements CurrentUserService
{
    @Override
    public UserId getCurrentUserId()
    {
        ClientUserDetails userDetails = (ClientUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return UserId.of(userDetails.getToDoUserId());
    }
}

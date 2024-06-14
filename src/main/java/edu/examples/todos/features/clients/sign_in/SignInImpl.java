package edu.examples.todos.features.clients.sign_in;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.features.clients.get_client_info.ClientInfoNotFoundException;
import edu.examples.todos.features.clients.shared.ClientInfo;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import edu.examples.todos.presentation.api.security.services.clients.ClientAuthority;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsService;
import edu.examples.todos.presentation.api.security.services.clients.ClientUserDetails;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.users.accounting.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SignInImpl implements SignIn
{
    private final ReactiveAuthenticationManager authenticationManager;
    private final ClientDetailsService clientDetailsService;
    private final UserRepository userRepository;
    private final UseCaseMapper useCaseMapper;

    @Override
    public Mono<SignInReply> run(@Valid SignInRequest request) throws NullPointerException, ClientInfoNotFoundException
    {
        return 
            authenticateClient(
                request.getClientId(), 
                request.getClientSecret()
            )
            .zipWhen(clientDetails -> getToDoUser(clientDetails.getUserId()))
            .map(v -> 
                SignInReply.of(
                    ClientInfo.of(
                        v.getT1().getId(),
                        v.getT1().getAuthorities().stream().map(ClientAuthority::getName).toList(),
                        useCaseMapper.map(v.getT2(), UserDto.class)
                    )
                )
            );
    }

    private Mono<User> getToDoUser(UserId toDoUserId)
    {
        return Mono.fromCallable(
            () -> userRepository
                    .findById(toDoUserId)
                    .orElseThrow(ClientInfoNotFoundException::new)
        );
    }

    private Mono<ClientDetails> authenticateClient(String clientId, String clientSecret)
    {
        var authenticationToken = (Authentication)UsernamePasswordAuthenticationToken.unauthenticated(clientId, clientSecret);

        return 
                authenticationManager
                    .authenticate(authenticationToken)
                    .map(v -> (ClientUserDetails)v.getPrincipal())
                    .map(ClientUserDetails::getUsername)
                    .flatMap(clientDetailsService::getClientDetails);
    }
}

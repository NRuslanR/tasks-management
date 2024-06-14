package edu.examples.todos.features.clients.sign_up;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import edu.examples.todos.domain.operations.creation.users.CreateUserReply;
import edu.examples.todos.domain.operations.creation.users.CreateUserRequest;
import edu.examples.todos.domain.operations.creation.users.UserCreationService;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.features.clients.shared.ClientInfo;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import edu.examples.todos.presentation.api.security.services.clients.ClientAuthority;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsNotFoundException;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsService;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.users.accounting.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Validated
@Transactional
public class StandardSignUpService implements SignUpService
{
    private final UserCreationService userCreationService;
    private final UserRepository userRepository;
    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UseCaseMapper useCaseMapper;

    @Override
    public Mono<SignUpReply> signUp(@Valid SignUpRequest request) throws NullPointerException, ClientAlreadyExistsException
    {
        return 
            ensureClientDoesNotExistsYet(request.getClientId())
                .flatMap(v -> createToDoUser(request))
                .zipWhen(user -> createClientDetailsFor(request, user))
                .map(v -> 
                    SignUpReply.of(
                        ClientInfo.of(
                                v.getT2().getId(),
                                v.getT2().getAuthorityNames(),
                                useCaseMapper.map(v.getT1(), UserDto.class)
                        )
                    )
                );
    }

    private Mono<String> ensureClientDoesNotExistsYet(String clientId) 
    {
        return Mono.fromCallable(() -> {

            try {

                clientDetailsService.getClientDetails(clientId).block();
    
                throw new ClientAlreadyExistsException();
            }
    
            catch (ClientDetailsNotFoundException exception)
            {
                return clientId;
            }

        });
    }

    private Mono<User> createToDoUser(SignUpRequest request)
    {
        var createUserRequest = useCaseMapper.map(request, CreateUserRequest.class);

        return 
            userCreationService
                .createUserAsync(createUserRequest)
                .map(CreateUserReply::getUser)
                .map(userRepository::save);
    }

    private Mono<ClientDetails> createClientDetailsFor(SignUpRequest request, User user)
    {
        var clientDetails = useCaseMapper.map(request, ClientDetails.class);

        clientDetails.setSecret(passwordEncoder.encode(clientDetails.getSecret()));
        clientDetails.setUserId(user.getId());
        clientDetails.setAuthorities(Set.of(ClientAuthority.of("USER"))); // inject service to get the default role set for registered clients

        return clientDetailsService.createClientDetails(clientDetails);
    }
}

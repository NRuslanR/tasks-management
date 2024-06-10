package edu.examples.todos.features.clients.sign_up;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

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
    public SignUpReply signUp(@Valid SignUpRequest request) throws NullPointerException, ClientAlreadyExistsException
    {
        ensureClientDoesNotExistsYet(request.getClientId());

        User user = createToDoUser(request);
        ClientDetails clientDetails = createClientDetailsFor(request, user);

        return SignUpReply.of(
                ClientInfo.of(
                        clientDetails.getId(),
                        clientDetails.getAuthorityNames(),
                        useCaseMapper.map(user, UserDto.class)
                )
        );
    }

    private void ensureClientDoesNotExistsYet(String clientId) {

        try {

            clientDetailsService.getClientDetails(clientId);

            throw new ClientAlreadyExistsException();
        }

        catch (ClientDetailsNotFoundException exception)
        {

        }
    }

    private User createToDoUser(SignUpRequest request)
    {
        var createUserRequest = useCaseMapper.map(request, CreateUserRequest.class);

        var user = userCreationService.createUserAsync(createUserRequest).block().getUser();

        return userRepository.save(user);
    }

    private ClientDetails createClientDetailsFor(SignUpRequest request, User user)
    {
        var clientDetails = useCaseMapper.map(request, ClientDetails.class);

        clientDetails.setSecret(passwordEncoder.encode(clientDetails.getSecret()));
        clientDetails.setUserId(user.getId());
        clientDetails.setAuthorities(Set.of(ClientAuthority.of("USER"))); // inject service to get the default role set for registered clients

        return clientDetailsService.createClientDetails(clientDetails);
    }
}

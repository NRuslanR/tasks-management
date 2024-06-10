package edu.examples.todos.features.clients.sign_in;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInImpl implements SignIn
{
    private final AuthenticationManager authenticationManager;
    private final ClientDetailsService clientDetailsService;
    private final UserRepository userRepository;
    private final UseCaseMapper useCaseMapper;

    @Override
    public SignInReply run(@Valid SignInRequest request) throws NullPointerException, ClientInfoNotFoundException
    {
        var clientDetails = authenticateClient(request.getClientId(), request.getClientSecret());

        var toDoUser = getToDoUser(clientDetails.getUserId());

        return SignInReply.of(
            ClientInfo.of(
                clientDetails.getId(),
                clientDetails.getAuthorities().stream().map(ClientAuthority::getName).toList(),
                useCaseMapper.map(toDoUser, UserDto.class)
            )
        );
    }

    private User getToDoUser(UserId toDoUserId)
    {
        return userRepository.findById(toDoUserId).orElseThrow(ClientInfoNotFoundException::new);
    }

    private ClientDetails authenticateClient(String clientId, String clientSecret)
    {
        var authenticationToken = (Authentication)UsernamePasswordAuthenticationToken.unauthenticated(clientId, clientSecret);

        authenticationToken = authenticationManager.authenticate(authenticationToken);

        var clientUserDetails = (ClientUserDetails)authenticationToken.getPrincipal();

        return clientDetailsService.getClientDetails(clientUserDetails.getUsername())   ;
    }
}

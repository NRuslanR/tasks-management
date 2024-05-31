package edu.examples.todos.features.clients.get_client_info;

import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.features.clients.shared.ClientInfo;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsRepository;
import edu.examples.todos.usecases.common.mapping.UseCaseMapper;
import edu.examples.todos.usecases.users.accounting.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetClientInfoImpl implements GetClientInfo
{
    private final ClientDetailsRepository clientDetailsRepository;
    private final UserRepository userRepository;
    private final UseCaseMapper useCaseMapper;

    @Override
    public GetClientInfoResult run(@Valid GetClientInfoQuery query) throws NullPointerException, ClientInfoNotFoundException
    {
        var clientDetails = getClientDetails(query.getClientId());

        var toDoUser = getToDoUser(clientDetails.getUserId());

        return GetClientInfoResult.of(
                ClientInfo.of(
                        clientDetails.getId(),
                        clientDetails.getAuthorityNames(),
                        useCaseMapper.map(toDoUser, UserDto.class)
                )
        );
    }
    private ClientDetails getClientDetails(String clientId)
    {
        return clientDetailsRepository.findById(clientId).orElseThrow(ClientInfoNotFoundException::new);
    }

    private User getToDoUser(UserId userId)
    {
        return userRepository.findById(userId).orElseThrow(ClientInfoNotFoundException::new);
    }
}

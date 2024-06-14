package edu.examples.todos.features.clients.get_client_info;

import org.springframework.stereotype.Service;

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
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetClientInfoImpl implements GetClientInfo
{
    private final ClientDetailsRepository clientDetailsRepository;
    private final UserRepository userRepository;
    private final UseCaseMapper useCaseMapper;

    @Override
    public Mono<GetClientInfoResult> run(@Valid GetClientInfoQuery query) throws NullPointerException, ClientInfoNotFoundException
    {
        return 
            getClientDetails(query.getClientId())
                .zipWhen(clientDetails -> getToDoUser(clientDetails.getUserId()))
                .map(v ->
                    GetClientInfoResult.of(
                        ClientInfo.of(
                            v.getT1().getId(),
                            v.getT1().getAuthorityNames(),
                            useCaseMapper.map(v.getT2(), UserDto.class)
                        )
                    )
                );
    }

    private Mono<ClientDetails> getClientDetails(String clientId)
    {
        return Mono.fromCallable(
            () -> clientDetailsRepository
                    .findById(clientId)
                    .orElseThrow(ClientInfoNotFoundException::new)
        );
    }

    private Mono<User> getToDoUser(UserId userId)
    {
        return Mono.fromCallable(
            () -> userRepository
                    .findById(userId)
                    .orElseThrow(ClientInfoNotFoundException::new)
        );
    }
}

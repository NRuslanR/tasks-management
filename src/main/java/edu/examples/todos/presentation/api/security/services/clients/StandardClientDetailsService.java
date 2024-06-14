package edu.examples.todos.presentation.api.security.services.clients;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Validated
public class StandardClientDetailsService implements ClientDetailsService, ReactiveUserDetailsService
{
    private final ClientDetailsRepository clientDetailsRepository;

    @Override
    public Mono<ClientDetails> getClientDetails(@NotBlank String clientId) throws NullPointerException, ClientDetailsNotFoundException
    {
        return getClientDetailsOrThrow(clientId, ClientDetailsNotFoundException.class);
    }

    @Override
    public Mono<ClientDetails> createClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException
    {
        return Mono.fromCallable(() -> clientDetailsRepository.save(clientDetails));
    }

    @Override
    public Mono<ClientDetails> updateClientDetails(@Valid ClientDetails newClientDetails) throws NullPointerException, ClientDetailsNotFoundException
    {
        var currentClientDetails = getClientDetails(newClientDetails.getId());

        return Mono.fromCallable(() -> clientDetailsRepository.save(newClientDetails));
    }

    @Override
    public Mono<Void> removeClientDetails(@NotBlank String clientId) throws NullPointerException
    {
        return Mono.fromRunnable(() -> clientDetailsRepository.deleteById(clientId));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) 
    {
        return
            getClientDetailsOrThrow(
                username,
                UsernameNotFoundException.class,
                ClientDetailsNotFoundException.MESSAGE_CONTENT
            ).map(clientDetails ->
                new ClientUserDetails(
                    clientDetails.getId(),
                    clientDetails.getSecret(),
                    clientDetails.getUserId().getValue().toString(),
                    clientDetails
                        .getAuthorities()
                        .stream()
                        .map(v -> "ROLE_" + v.getName())
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                )
            );
    }

    private Mono<ClientDetails> getClientDetailsOrThrow(String clientId, Class<?> exceptionClass)
    {
        return getClientDetailsOrThrow(clientId, exceptionClass, "");
    }

    @SneakyThrows
    private Mono<ClientDetails> getClientDetailsOrThrow(String clientId, Class<?> exceptionClass, String message)
    {
        return Mono.fromCallable(() -> clientDetailsRepository.findById(clientId).orElseThrow(() -> createException(exceptionClass, message)));
    }

    @SneakyThrows
    private Exception createException(Class<?> exceptionClass, String message)
    {
        var exception =
                StringUtils.hasText(message) ?
                        exceptionClass.getDeclaredConstructor(String.class).newInstance(message) :
                        exceptionClass.getDeclaredConstructor().newInstance();

        return (Exception)exception;
    }
}

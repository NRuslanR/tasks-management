package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
public class StandardClientDetailsService implements ClientDetailsService, UserDetailsService
{
    private final ClientDetailsRepository clientDetailsRepository;

    @Override
    public ClientDetails getClientDetails(@NotBlank String clientId) throws NullPointerException, ClientDetailsNotFoundException
    {
        return getClientDetailsOrThrow(clientId, ClientDetailsNotFoundException.class);
    }

    @Override
    public ClientDetails createClientDetails(@Valid ClientDetails clientDetails) throws NullPointerException
    {
        return clientDetailsRepository.save(clientDetails);
    }

    @Override
    public ClientDetails updateClientDetails(@Valid ClientDetails newClientDetails) throws NullPointerException, ClientDetailsNotFoundException
    {
        var currentClientDetails = getClientDetails(newClientDetails.getId());

        return clientDetailsRepository.save(newClientDetails);
    }

    @Override
    public void removeClientDetails(@NotBlank String clientId) throws NullPointerException
    {
        clientDetailsRepository.deleteById(clientId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var clientDetails =
                getClientDetailsOrThrow(
                        username,
                        UsernameNotFoundException.class,
                        ClientDetailsNotFoundException.MESSAGE_CONTENT
                );

        return
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
                );
    }

    private ClientDetails getClientDetailsOrThrow(String clientId, Class<?> exceptionClass)
    {
        return getClientDetailsOrThrow(clientId, exceptionClass, "");
    }

    @SneakyThrows
    private ClientDetails getClientDetailsOrThrow(String clientId, Class<?> exceptionClass, String message)
    {
        return clientDetailsRepository.findById(clientId).orElseThrow(() -> createException(exceptionClass, message));
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

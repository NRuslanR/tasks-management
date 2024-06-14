package edu.examples.todos.presentation.api.security.authentication.filters;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;

import edu.examples.todos.features.clients.shared.ClientInfo;
import edu.examples.todos.features.clients.shared.ClientInfoResource;
import edu.examples.todos.features.clients.sign_in.SignInResponse;
import edu.examples.todos.features.clients.sign_up.SignUpResponse;
import edu.examples.todos.presentation.api.security.services.jwt.BaseJwtTokenInfo;
import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

public class JwtAuthorizationHeaderSetFilter extends ResponseBodyResultHandler
{
    private static MethodParameter param;

    static 
    {
        param = createParam();
    }

    @SneakyThrows
    private static MethodParameter createParam()
    {
        return new MethodParameter(JwtAuthorizationHeaderSetFilter.class
                    .getDeclaredMethod("methodForParams"), -1);
    }
    
    private static Mono<Object> methodForParams() {
        return null;
    }

    @Autowired
    private JwtService jwtService;

    public JwtAuthorizationHeaderSetFilter(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    @Override
    public boolean supports(HandlerResult result) 
    {
        if (!result.getReturnType().resolve().equals(Mono.class))
            return false;

        var returnedObjectType = result.getReturnType().resolveGeneric(0);

        return 
            !Objects.isNull(returnedObjectType) &&
            (returnedObjectType.equals(SignInResponse.class) ||
            returnedObjectType.equals(SignUpResponse.class));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public Mono<Void> handleResult(
        ServerWebExchange exchange, 
        HandlerResult result
    ) 
    {
        Mono<Object> body = (Mono<Object>)result.getReturnValue();

        var newBody =
            body
                .zipWhen(v -> Mono.just(getClientInfoResource(v)))
                .map(v -> {

                    var originalBody = v.getT1();

                    var accessToken = createJwtTokenString(v.getT2().getContent());

                    exchange.getResponse().getHeaders().setBearerAuth(accessToken);

                    return originalBody;
                });

        return writeBody(newBody, param, exchange);
    }

    @SneakyThrows
    private ClientInfoResource getClientInfoResource(Object body)
    {
        return (ClientInfoResource)body.getClass().getMethod("getClientInfoResource").invoke(body);
    }

    private String createJwtTokenString(ClientInfo clientInfo)
    {
        return 
            jwtService.createJwtTokenString(
                BaseJwtTokenInfo.of(
                    clientInfo.getClientId(),
                    Map.of("roles", clientInfo.getClientAuthorities())
                )
            );
    }
}

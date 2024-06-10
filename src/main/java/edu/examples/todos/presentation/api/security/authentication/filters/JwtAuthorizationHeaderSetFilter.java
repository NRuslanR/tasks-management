package edu.examples.todos.presentation.api.security.authentication.filters;

import edu.examples.todos.features.clients.shared.ClientInfoResource;
import edu.examples.todos.features.clients.sign_in.SignInResponse;
import edu.examples.todos.features.clients.sign_up.SignUpResponse;
import edu.examples.todos.presentation.api.security.services.jwt.BaseJwtTokenInfo;
import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "jwt")
@ControllerAdvice
@RequiredArgsConstructor
public class JwtAuthorizationHeaderSetFilter implements ResponseBodyAdvice<Object>
{
    private final JwtService jwtService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    )
    {
        if (!(body instanceof SignInResponse || body instanceof SignUpResponse))
            return body;

        var clientInfoResource = (ClientInfoResource)body.getClass().getMethod("getClientInfoResource").invoke(body);

        var clientInfo = clientInfoResource.getContent();

        var accessToken =
                jwtService.createJwtTokenString(
                        BaseJwtTokenInfo.of(
                                clientInfo.getClientId(),
                                Map.of("roles", clientInfo.getClientAuthorities())
                        )
                );

        response.getHeaders().add(HttpHeaders.AUTHORIZATION, accessToken);

        return body;
    }
}

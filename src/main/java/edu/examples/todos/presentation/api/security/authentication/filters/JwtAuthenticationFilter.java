package edu.examples.todos.presentation.api.security.authentication.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter
{
    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) 
    {
        var request = exchange.getRequest();

        var jwtTokenString = extractJwtTokenString(request);

        if (!StringUtils.hasText(jwtTokenString))
            return filterChain.filter(exchange);

        var tokenInfo = jwtService.validateJwtTokenString(jwtTokenString);

        return 
            filterChain
                .filter(exchange)
                .contextWrite(context -> 
                    ReactiveSecurityContextHolder
                        .withSecurityContext(
                            getUserDetailsFor(tokenInfo.getSubject())
                            .map(userDetails -> createAuthenticationPrincipal(userDetails, request))
                            .map(SecurityContextImpl::new)
                        )
                );
    }

    private String extractJwtTokenString(ServerHttpRequest request)
    {
        var authorizationBearer = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authorizationBearer))
            return "";

        var bearerParts = authorizationBearer.split(" ");

        if (bearerParts.length != 2)
            return "";
        
        var bearerLabel = bearerParts[0];
        var bearerToken = bearerParts[1];

        if (!bearerLabel.equals("Bearer") || !StringUtils.hasText(bearerToken))
            return "";

        return bearerToken;
    }

    private Mono<UserDetails> getUserDetailsFor(String subject)
    {
        return userDetailsService.findByUsername(subject);
    }

    private Authentication createAuthenticationPrincipal(UserDetails userDetails, ServerHttpRequest request)
    {
        var authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        return authentication;
    }
}

package edu.examples.todos.presentation.api.security.authentication.filters;

import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException
    {
        var jwtTokenString = extractJwtTokenString(request);

        if (!StringUtils.hasText(jwtTokenString))
        {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenInfo = jwtService.validateJwtTokenString(jwtTokenString);

        var userDetails = getUserDetailsFor(tokenInfo.getSubject());

        setCurrentAuthenticationPrincipal(userDetails, request);

        filterChain.doFilter(request, response);
    }

    private String extractJwtTokenString(HttpServletRequest request)
    {
        var authorizationBearer = request.getHeader(HttpHeaders.AUTHORIZATION);

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

    private UserDetails getUserDetailsFor(String subject)
    {
        return userDetailsService.loadUserByUsername(subject);
    }

    private void setCurrentAuthenticationPrincipal(UserDetails userDetails, HttpServletRequest request)
    {
        var authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

package edu.examples.todos.presentation.api.security.authentication.exception_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    private final ObjectMapper jacksonMapper;

    private final String realmName;;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException
    {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(
                jacksonMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(new ApplicationError(authException.getMessage()))
        );
    }
}

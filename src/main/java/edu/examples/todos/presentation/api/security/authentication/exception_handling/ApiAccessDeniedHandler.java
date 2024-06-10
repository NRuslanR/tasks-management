package edu.examples.todos.presentation.api.security.authentication.exception_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiAccessDeniedHandler implements AccessDeniedHandler
{
    private final ObjectMapper jacksonMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException
    {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(
                jacksonMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(new ApplicationError(accessDeniedException.getMessage()))
        );
    }
}

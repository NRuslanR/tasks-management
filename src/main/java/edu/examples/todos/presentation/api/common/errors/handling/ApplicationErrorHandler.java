package edu.examples.todos.presentation.api.common.errors.handling;

import edu.examples.todos.domain.actors.todos.ToDoActionIsNotAvailableException;
import edu.examples.todos.features.shared.FeatureException;
import edu.examples.todos.presentation.api.common.errors.ApplicationError;
import edu.examples.todos.presentation.api.common.exceptions.EntityNotFoundUseCaseException;
import edu.examples.todos.usecases.common.exceptions.UseCasesException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order
@Slf4j
public class ApplicationErrorHandler extends ResponseEntityExceptionHandler
{
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @ExceptionHandler({
            UseCasesException.class,
            FeatureException.class
    })
    public ResponseEntity<ApplicationError> handleUseCasesException(Exception exception)
    {
        return ResponseEntity.badRequest().body(new ApplicationError(exception.getMessage()));
    }

    @SneakyThrows
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception,
            AuthenticationEntryPoint authenticationEntryPoint
    )
    {
        authenticationEntryPoint.commence(request, response, exception);
    }

    @SneakyThrows
    @ExceptionHandler({
            AccessDeniedException.class,
            ToDoActionIsNotAvailableException.class
    })
    public void handleAccessDeniedException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception
    )
    {
        var accessDeniedException =
                exception instanceof AccessDeniedException ?
                        (AccessDeniedException)exception :
                        new AccessDeniedException(exception.getMessage());

        accessDeniedHandler.handle(request, response, accessDeniedException);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    )
    {
        return ResponseEntity.badRequest().body(
                new ApplicationError(
                        StringUtils.collectionToDelimitedString(
                            ex
                                .getBindingResult()
                                    .getFieldErrors()
                                    .stream()
                                    .map(FieldError::getDefaultMessage)
                                    .toList(),
                      "."
                        )
                )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError handleConstraintViolationException(ConstraintViolationException exception)
    {
        return new ApplicationError(exception.getMessage());
    }

    @ExceptionHandler({
            EntityNotFoundUseCaseException.class
    })
    public ResponseEntity<ApplicationError> handleEntityNotFoundUseCaseException(EntityNotFoundUseCaseException exception)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApplicationError(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleApplicationException(Exception exception)
    {
        log.error("", exception);

        return ResponseEntity.internalServerError().body(
                new ApplicationError("The internal error occurred during the handling this request")
        );
    }
}

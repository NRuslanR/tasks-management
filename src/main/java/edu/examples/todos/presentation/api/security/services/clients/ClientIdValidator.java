package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ClientIdValidator implements ConstraintValidator<ClientId, String>
{
    private static final String ID_PATTERN = "^(?=.*[0-9a-zA-Z])[a-zA-Z][a-zA-Z\\d\\.-]{0,19}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        if (!StringUtils.hasText(value))
            return false;

        var idPattern = Pattern.compile(ID_PATTERN);

        var idMatcher = idPattern.matcher(value);

        return idMatcher.matches();
    }
}

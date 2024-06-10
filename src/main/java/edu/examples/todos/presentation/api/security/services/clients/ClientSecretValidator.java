package edu.examples.todos.presentation.api.security.services.clients;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ClientSecretValidator implements ConstraintValidator<ClientSecret, String>
{
    private static final String SECRET_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        if (!StringUtils.hasText(value))
            return false;

        var secretPattern = Pattern.compile(SECRET_PATTERN, Pattern.CASE_INSENSITIVE);

        var secretMatcher = secretPattern.matcher(value);

        return secretMatcher.matches();
    }
}

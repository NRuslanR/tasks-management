package edu.examples.todos.usecases.common.mapping;

import edu.examples.todos.domain.common.exceptions.DomainException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Optional;

public abstract class AbstractUseCaseMapper implements UseCaseMapper
{
    @Override
    public <D> D map(Object source, Class<D> destinationType)
    {
        try
        {
            return doMap(source, destinationType);
        }

        catch (RuntimeException exception)
        {
            throw processException(exception);
        }
    }

    protected abstract <D> D doMap(Object source, Class<D> destinationType);

    @Override
    public <D> D map(Object source, D destination)
    {
        try
        {
            return doMap(source, destination);
        }

        catch (RuntimeException exception)
        {
            throw processException(exception);
        }
    }

    protected abstract <D> D doMap(Object source, D destination);

    private RuntimeException processException(RuntimeException exception)
    {
        return
            Optional.ofNullable(
                (RuntimeException)ExceptionUtils.throwableOfType(exception, DomainException.class)
            )
            .orElse(exception);
    }
}

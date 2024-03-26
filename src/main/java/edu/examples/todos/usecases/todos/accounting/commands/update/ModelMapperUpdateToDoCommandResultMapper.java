package edu.examples.todos.usecases.todos.accounting.commands.update;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.common.exceptions.DomainException;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModelMapperUpdateToDoCommandResultMapper implements UpdateToDoCommandResultMapper
{
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public ToDo changeToDoByCommand(ToDo toDo, UpdateToDoCommand command)
            throws NullPointerException, IncorrectUpdateToDoCommandException
    {
        try
        {
            modelMapper.map(command, toDo);
        }

        catch (RuntimeException exception)
        {
            var throwable =
                    Optional.ofNullable(
                            (Exception)ExceptionUtils.throwableOfType(exception, DomainException.class)
                    )
                    .map(Exception::getMessage)
                    .map(IncorrectUpdateToDoCommandException::new)
                    .map(v -> (RuntimeException)v)
                    .orElse(exception);

            throw throwable;
        }

        return toDo;
    }

    @Override
    public UpdateToDoResult toUpdateToDoResult(ToDo toDo)
    {
        return new UpdateToDoResult(modelMapper.map(toDo, ToDoDto.class));
    }
}

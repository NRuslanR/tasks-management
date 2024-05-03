package edu.examples.todos.usecases.common.exceptions.translation;

import edu.examples.todos.domain.actors.todos.ToDoActionIsNotAvailableException;
import edu.examples.todos.domain.actors.todos.ToDoStateIsNotCorrectDomainException;
import edu.examples.todos.domain.operations.accounting.todos.ToDoNotFoundDomainException;
import edu.examples.todos.domain.operations.relationships.todos.DescendentToDoCanNotBeParentForAncestorToDoException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoNotFoundException;
import edu.examples.todos.usecases.todos.common.exceptions.ToDoStateIsNotCorrectException;
import edu.examples.todos.usecases.todos.relationships.commands.assign_parent.ToDoIsInCorrectToBeParentException;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StandardExceptionTranslator implements ExceptionTranslator
{
    private Map<Class<? extends Exception>, Class<? extends Exception>> translationMap;

    public StandardExceptionTranslator()
    {
        translationMap = new HashMap<>();

        initTranslationMap();
    }

    private void initTranslationMap()
    {
        translationMap.put(ToDoNotFoundDomainException.class, ToDoNotFoundException.class);
        translationMap.put(ToDoStateIsNotCorrectDomainException.class, ToDoStateIsNotCorrectException.class);
        translationMap.put(ToDoActionIsNotAvailableException.class, ToDoStateIsNotCorrectException.class);
        translationMap.put(DescendentToDoCanNotBeParentForAncestorToDoException.class, ToDoIsInCorrectToBeParentException.class);
    }

    @Override
    public Exception translateException(@NonNull Exception source)
    {
        return
                Optional
                        .ofNullable(translationMap.get(source.getClass()))
                        .map(v -> createException(v, source))
                        .orElse(source);
    }

    @SneakyThrows
    private Exception createException(Class<? extends Exception> exceptionClass, Exception sourceException)
    {
        return exceptionClass.getDeclaredConstructor(String.class).newInstance(sourceException.getMessage());
    }
}

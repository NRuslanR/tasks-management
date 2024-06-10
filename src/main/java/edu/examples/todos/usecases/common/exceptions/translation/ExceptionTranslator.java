package edu.examples.todos.usecases.common.exceptions.translation;

public interface ExceptionTranslator
{
    Exception translateException(Exception source);
}

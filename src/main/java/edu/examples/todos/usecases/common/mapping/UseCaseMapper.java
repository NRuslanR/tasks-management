package edu.examples.todos.usecases.common.mapping;

public interface UseCaseMapper
{
    <D> D map(Object source, Class<D> destinationType);

    <D> D map(Object source, D destination);
}

package edu.examples.todos.usecases.common.mapping;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperUseCaseMapper extends AbstractUseCaseMapper
{
    private final ModelMapper modelMapper;

    @Override
    protected <D> D doMap(Object source, Class<D> destinationType)
    {
        return modelMapper.map(source, destinationType);
    }

    @Override
    protected <D> D doMap(Object source, D destination)
    {
        modelMapper.map(source, destination);

        return destination;
    }
}

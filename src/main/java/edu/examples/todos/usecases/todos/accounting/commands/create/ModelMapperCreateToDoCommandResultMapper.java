package edu.examples.todos.usecases.todos.accounting.commands.create;

import edu.examples.todos.domain.operations.creation.todos.CreateToDoReply;
import edu.examples.todos.domain.operations.creation.todos.CreateToDoRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperCreateToDoCommandResultMapper implements CreateToDoCommandResultMapper
{
    private final ModelMapper mapper;

    @Override
    public CreateToDoRequest toCreateToDoRequest(CreateToDoCommand createToDoCommand)
    {
        return mapper.map(createToDoCommand, CreateToDoRequest.class);
    }

    @Override
    public CreateToDoResult toCreateToDoResult(CreateToDoReply createToDoReply)
    {
        return mapper.map(createToDoReply, CreateToDoResult.class);
    }
}

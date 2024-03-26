package edu.examples.todos.usecases.todos.common.mapping.config;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.usecases.todos.accounting.ToDoDto;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperMappingConfig
{
    @Bean
    public ModelMapper modelMapper()
    {
        var mapper = createModelMapper();

        customizeMappings(mapper);

        return mapper;
    }

    private ModelMapper createModelMapper()
    {
        var mapper = new ModelMapper();

        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        return mapper;
    }

    private void customizeMappings(ModelMapper mapper)
    {
        customizeToDoMappings(mapper);
    }

    private void customizeToDoMappings(ModelMapper mapper)
    {
        var toDoDtoMap = mapper.createTypeMap(ToDo.class, ToDoDto.class);

        Converter<ToDoId, String> idConverter =
            ctx -> {

                return ctx.getSource().getValue().toString();

            };

        toDoDtoMap.addMappings(m -> m.using(idConverter).map(ToDo::getId, ToDoDto::setId));

        var updateToDoMap = mapper.createTypeMap(UpdateToDoCommand.class, ToDo.class);
    }
}

package edu.examples.todos.usecases.todos.common.mapping.config;

import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.usecases.todos.accounting.dtos.ToDoDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
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
        var typeMap = mapper.createTypeMap(ToDo.class, ToDoDto.class);

        Converter<ToDoId, String> idConverter =
            ctx -> {

                return ctx.getSource().getValue().toString();

            };

        typeMap.addMappings(m -> m.using(idConverter).map(ToDo::getId, ToDoDto::setId));
    }
}

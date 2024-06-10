package edu.examples.todos.usecases.todos.accounting.queries.common;

import edu.examples.todos.usecases.todos.common.dtos.ToDoActionsAvailabilityDto;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDisplayStateResolver;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JdbcToDoDtoMapper extends DataClassRowMapper<ToDoDto>
{
    private final DataClassRowMapper<ToDoActionsAvailabilityDto> actionsAvailabilityDtoMapper =
            new DataClassRowMapper<>(ToDoActionsAvailabilityDto.class);

//    private final DataClassRowMapper<UserDto> userDtoMapper =
//            new DataClassRowMapper<>(UserDto.class);

    private final ToDoDisplayStateResolver toDoDisplayStateResolver;

    public JdbcToDoDtoMapper(ToDoDisplayStateResolver toDoDisplayStateResolver)
    {
        super(ToDoDto.class);

        this.toDoDisplayStateResolver = toDoDisplayStateResolver;
    }

    @Override
    public ToDoDto mapRow(ResultSet rs, int rowNumber) throws SQLException
    {
        var toDoDto = super.mapRow(rs, rowNumber);

        toDoDto.setState(toDoDto.getState().toLowerCase());
        toDoDto.setActionsAvailability(actionsAvailabilityDtoMapper.mapRow(rs, rowNumber));
        toDoDto.setDisplayState(toDoDisplayStateResolver.resolveDisplayState(toDoDto.getState()));
        //toDoDto.setAuthor(userDtoMapper.mapRow(rs, rowNumber));
        return toDoDto;
    }
}
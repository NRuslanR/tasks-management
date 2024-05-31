package edu.examples.todos.usecases.common.mapping.config;

import edu.examples.todos.domain.actors.todos.OperableToDo;
import edu.examples.todos.domain.actors.todos.ToDo;
import edu.examples.todos.domain.actors.todos.ToDoId;
import edu.examples.todos.domain.actors.todos.ToDoPriority;
import edu.examples.todos.domain.operations.creation.todos.CreateToDoRequest;
import edu.examples.todos.domain.operations.creation.users.CreateUserRequest;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.domain.resources.users.UserName;
import edu.examples.todos.features.clients.sign_up.SignUpRequest;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.usecases.todos.accounting.commands.create.CreateToDoCommand;
import edu.examples.todos.usecases.todos.accounting.commands.update.UpdateToDoCommand;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDisplayStateResolver;
import edu.examples.todos.usecases.todos.common.dtos.ToDoDto;
import edu.examples.todos.usecases.users.accounting.UserDto;
import edu.examples.todos.usecases.users.accounting.commands.create.CreateUserCommand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/*
refactor: split on multiple configs by domain aggregates, use-cases commands, sign-up, sign-in requests and etc
 */
@Configuration
@RequiredArgsConstructor
public class ModelMapperMappingConfig
{
    private final ToDoDisplayStateResolver toDoDisplayStateResolver;

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
        customizeCommandsMappings(mapper);
        customizeDtosMappings(mapper);
    }

    private void customizeCommandsMappings(ModelMapper mapper)
    {
        customizeToDoCommandsMappings(mapper);
        customizeUserCommandsMappings(mapper);
    }

    private void customizeToDoCommandsMappings(ModelMapper mapper)
    {
        var createToDoRequestMap =
                mapper.createTypeMap(CreateToDoCommand.class, CreateToDoRequest.class);

        Converter<CreateToDoCommand, CreateToDoRequest> createToDoCommandConverter =
                ctx -> {

                    var source = ctx.getSource();

                    ctx.getDestination().setPriority(

                            StringUtils.hasText(source.getPriorityType()) ?
                                    Optional.of(
                                            ToDoPriority.of(
                                                    source.getPriorityType(),
                                                    source.getPriorityValue().get()
                                            )
                                    ) : Optional.empty()
                    );

                    return ctx.getDestination();
                };

        createToDoRequestMap.setPostConverter(createToDoCommandConverter);

        var toDoMap = mapper.createTypeMap(UpdateToDoCommand.class, ToDo.class);

        Converter<UpdateToDoCommand, ToDo> updateToDoCommandToDoConverter =
                ctx -> {

                    var source = ctx.getSource();

                    if (!StringUtils.hasText(source.getPriorityType()))
                        return ctx.getDestination();

                    ctx
                            .getDestination()
                            .setPriority(
                                    ToDoPriority.of(
                                            source.getPriorityType(),
                                            source.getPriorityValue().get()
                                    )
                            );

                    return  ctx.getDestination();
                };

        toDoMap.setPostConverter(updateToDoCommandToDoConverter);
    }

    private void customizeUserCommandsMappings(ModelMapper mapper)
    {
        var createUserMap = mapper.createTypeMap(CreateUserCommand.class, CreateUserRequest.class);

        Converter<CreateUserCommand, CreateUserRequest> createUserCommandConverter =
                ctx -> {
                    var source = ctx.getSource();
                    var destination = ctx.getDestination();

                    destination.setName(UserName.of(source.getFirstName(), source.getLastName()));

                    return destination;
                };

        createUserMap.setPostConverter(createUserCommandConverter);

        var signUpCreateUserMap = mapper.createTypeMap(SignUpRequest.class, CreateUserRequest.class);

        Converter<SignUpRequest, CreateUserRequest> signUpCreateUserConverter =
                ctx -> {
                    var source = ctx.getSource();
                    var destination = ctx.getDestination();

                    destination.setName(UserName.of(source.getFirstName(), source.getLastName()));

                    return destination;
                };

        signUpCreateUserMap.setPostConverter(signUpCreateUserConverter);
    }

    private void customizeDtosMappings(ModelMapper mapper)
    {
        customizeUserMappings(mapper);
        customizeClientDetailsMappings(mapper);
        customizeToDoMappings(mapper);
    }

    private void customizeClientDetailsMappings(ModelMapper mapper)
    {
        var clientDetailsMap = mapper.createTypeMap(SignUpRequest.class, ClientDetails.class);

        clientDetailsMap.addMapping(SignUpRequest::getClientId, ClientDetails::setId);
        clientDetailsMap.addMapping(SignUpRequest::getClientSecret, ClientDetails::setSecret);
    }

    private void customizeUserMappings(ModelMapper mapper)
    {
        var userMap = mapper.createTypeMap(User.class, UserDto.class);

        Converter<UserId, String> idConverter = ctx -> ctx.getSource().getValue().toString();


        userMap.addMappings(
            m -> {
                m.using(idConverter).map(User::getId, UserDto::setId);
                m.map(User::allowedToDoCreationCount, UserDto::setAllowedToDoCreationCount);
                m.map(User::canEditForeignTodos, UserDto::setEditForeignTodosAllowed);
                m.map(User::canRemoveForeignTodos, UserDto::setRemoveForeignTodosAllowed);
                m.map(User::canPerformForeignTodos, UserDto::setPerformForeignTodosAllowed);
            }
        );
    }

    private void customizeToDoMappings(ModelMapper mapper)
    {
        var toDoDtoMap = mapper.createTypeMap(ToDo.class, ToDoDto.class);

        Converter<ToDoId, String> idConverter =
            ctx -> {

                if (Objects.isNull(ctx.getSource()))
                    return "";

                return ctx.getSource().getValue().toString();

            };

        Converter<ToDoPriority, String> priorityTypeConverter =
            ctx -> {

                return ctx.getSource().type().toString();
            };

        Converter<ToDoPriority, Integer> priorityValueConverter =
            ctx -> {

                return ctx.getSource().value();
            };

        Converter<Enum, String> enumStringConverter =
                ctx -> ctx.getSource().toString().toLowerCase();

        toDoDtoMap.addMappings(
            m -> {
                m.using(idConverter).map(ToDo::getId, ToDoDto::setId);
                m.using(idConverter).map(ToDo::getParentToDoId, ToDoDto::setParentToDoId);
                m.using(priorityTypeConverter).map(ToDo::getPriority, ToDoDto::setPriorityType);
                m.using(priorityValueConverter).map(ToDo::getPriority, ToDoDto::setPriorityValue);
                m.using(enumStringConverter).map(ToDo::getState, ToDoDto::setState);
            }
        );

        toDoDtoMap.setPostConverter(ctx -> {

           var toDoDto = ctx.getDestination();

           toDoDto.setDisplayState(toDoDisplayStateResolver.resolveDisplayState(toDoDto.getState()));

           return toDoDto;

        });

        var operableToDoMap = mapper.createTypeMap(OperableToDo.class, ToDoDto.class);

        operableToDoMap.setPostConverter(ctx -> {

            var operableToDo = ctx.getSource();
            var toDoDto = ctx.getDestination();

            mapper.map(operableToDo.getTarget(), toDoDto);

            return toDoDto;
        });
    }
}

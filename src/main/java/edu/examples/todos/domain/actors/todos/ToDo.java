package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todos")
public class ToDo extends BaseEntity<ToDoId>
{
    public static ToDo of(ToDoId id, String name, String description, LocalDateTime createdAt)
    {
        return new ToDo(id, name, description, createdAt);
    }

    public static ToDo of(ToDoId id, String name, LocalDateTime createdAt)
    {
        return new ToDo(id, name, createdAt);
    }

    public ToDo(ToDoId id, String name, String description, LocalDateTime createdAt) throws ToDoNameInCorrectException
    {
        this(id, name, createdAt);

        setDescription(description);
    }

    public ToDo(ToDoId id, String name, LocalDateTime createdAt)
    {
        super(id);

        setName(name);
        setCreatedAt(createdAt);
    }

    protected ToDo()
    {

    }

    private String name;

    private void setName(String newName) throws ToDoNameInCorrectException
    {
        if (!StringUtils.hasText(newName))
        {
            throw new ToDoNameInCorrectException();
        }

        name = newName;
    }

    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.PRIVATE)
    @NonNull
    private LocalDateTime createdAt;

    private ToDoId parentToDoId;
}

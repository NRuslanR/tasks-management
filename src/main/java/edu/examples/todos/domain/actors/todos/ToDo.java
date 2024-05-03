package edu.examples.todos.domain.actors.todos;

import edu.examples.todos.domain.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/* refactor: it would be possible to divide this entity to multiple ones, each one per status */
@Data
@Entity
@Table(name = "todos")
public class ToDo extends BaseEntity<ToDoId>
{
    public static ToDo of(ToDoId id, String name, String description, ToDoPriority priority, LocalDateTime createdAt)
    {
        return new ToDo(id, name, description, priority, createdAt);
    }

    public static ToDo of(ToDoId id, String name, ToDoPriority priority, LocalDateTime createdAt)
    {
        return new ToDo(id, name, priority, createdAt);
    }

    public ToDo(ToDoId id, String name, String description, ToDoPriority priority, LocalDateTime createdAt) throws ToDoNameInCorrectException
    {
        this(id, name, priority, createdAt);

        setDescription(description);
    }

    private ToDo(ToDoId id, String name, ToDoPriority priority, LocalDateTime createdAt)
    {
        super(id);

        setCreatedState(createdAt);
        setName(name);
        setPriority(priority);
    }

    protected ToDo()
    {

    }

    private String name;

    public void setName(String newName) throws ToDoNameInCorrectException
    {
        ThrowIfToDoStateIsNotCorrectToBeChanged();

        if (!StringUtils.hasText(newName))
        {
            throw new ToDoNameInCorrectException();
        }

        name = newName;
    }

    @NonNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "type",
            column = @Column(name = "priority_type")
        ),
        @AttributeOverride(
            name = "value",
            column = @Column(name = "priority_value")
        )
    })
    private ToDoPriority priority;

    public void changePriorityType(ToDoPriorityType type)
    {
        setPriority(priority.changeType(type));
    }

    public void changePriorityValue(int value)
    {
        setPriority(priority.changeValue(value));
    }

    public void setPriority(ToDoPriority value)
    {
        ThrowIfToDoStateIsNotCorrectToBeChanged();

        priority = value;
    }

    private String description;

    public void setDescription(String value)
    {
        ThrowIfToDoStateIsNotCorrectToBeChanged();

        description = value;
    }

    /* refactor: */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "value",
                    column = @Column(name = "parentToDoId")
            )
    })
    /*
        In most use-cases sub-todos aren't required so in order to fall not into
        N + 1 problem and for performance this solution is used. Lazy makes To-Do model and tracing more complex
        and EntityGraph fetches redundant data that's question is why are sub-todos necessary if just
        current To-Do's name is required to change. This DDD approach
     */
    private ToDoId parentToDoId;

    public void setParentToDoId(ToDoId value)
    {
        ThrowIfToDoStateIsNotCorrectToBeChanged();

        parentToDoId = value;
    }

    @Enumerated(EnumType.STRING)
    @NonNull
    private ToDoState state;

    public void perform() throws ToDoStateIsNotCorrectDomainException
    {
        ThrowIfToDoStateIsNotCorrectToBePerformed();

        setPerformedState();
    }

    public boolean isPerformed()
    {
        return !Objects.isNull(performedAt);
    }

    private void ThrowIfToDoStateIsNotCorrectToBeChanged()
    {
        ThrowIfToDoStateIs(ToDoState.PERFORMED, "To-Do \"" + getName() + "\" can't be changed because it was performed");
    }

    private void ThrowIfToDoStateIsNotCorrectToBePerformed()
    {
        ThrowIfToDoStateIs(ToDoState.PERFORMED, "\"To-Do \"" + getName() + "\" is already performed\"");
    }

    private void ThrowIfToDoStateIs(ToDoState value)
    {
        ThrowIfToDoStateIs(value, "");
    }

    private void ThrowIfToDoStateIs(ToDoState value, String message)
    {
        if (state == value)
        {
            throw StringUtils.hasText(message) ? new ToDoStateIsNotCorrectDomainException(message) :
                    new ToDoStateIsNotCorrectDomainException();
        }
    }

    private void setCreatedState(LocalDateTime value)
    {
        state = ToDoState.CREATED;
        createdAt = adjustDate(value);
    }

    private void setPerformedState()
    {
        state = ToDoState.PERFORMED;
        performedAt = LocalDateTime.now();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private LocalDateTime createdAt;

    private void setCreatedAt(@NonNull LocalDateTime value)
    {
        createdAt = adjustDate(value);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private LocalDateTime performedAt;

    @PrePersist
    public void prePersist()
    {
        adjustDates();
    }

    @PreUpdate
    public void preUpdate()
    {
        adjustDates();
    }

    private void adjustDates()
    {
        createdAt = adjustDate(createdAt);
        performedAt = adjustDate(performedAt);
    }

    private LocalDateTime adjustDate(LocalDateTime value)
    {
        return Optional.ofNullable(value).map(v -> v.withNano(0)).orElse(null);
    }
}

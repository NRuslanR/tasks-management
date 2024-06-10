package edu.examples.todos.usecases.todos.accounting.commands.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateToDoCommand
{
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String priorityType;

    @Min(0)
    private Optional<Integer> priorityValue;

    private String authorId;

    public CreateToDoCommand withAuthorId(String authorId)
    {
        return CreateToDoCommand.of(
                getName(),
                getDescription(),
                getPriorityType(),
                getPriorityValue(),
                authorId
        );
    }
}

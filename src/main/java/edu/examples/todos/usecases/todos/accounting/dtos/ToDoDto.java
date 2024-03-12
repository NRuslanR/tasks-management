package edu.examples.todos.usecases.todos.accounting.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoDto
{
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}

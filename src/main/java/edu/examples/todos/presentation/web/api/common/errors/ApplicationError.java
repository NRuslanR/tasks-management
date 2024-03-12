package edu.examples.todos.presentation.web.api.common.errors;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("error")
public class ApplicationError
{
    private String message;
}
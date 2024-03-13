package edu.examples.todos.usecases.common;

import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

@SpringBootTest
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IntegrationUseCasesTest
{
}

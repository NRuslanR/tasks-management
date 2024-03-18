package edu.examples.todos.usecases.common.config;

import edu.examples.todos.common.config.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IntegrationTest
@SpringBootTest
public @interface IntegrationUseCasesTest
{
}

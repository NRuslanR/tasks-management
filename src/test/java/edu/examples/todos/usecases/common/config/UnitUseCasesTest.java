package edu.examples.todos.usecases.common.config;

import edu.examples.todos.common.config.UnitTest;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UnitTest
public @interface UnitUseCasesTest
{

}

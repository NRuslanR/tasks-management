package edu.examples.todos.persistence.common.config;

import edu.examples.todos.common.config.UnitTest;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UnitTest
public @interface UnitPersistenceTest
{
}

package edu.examples.todos.common.config.profiles;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "(.*)unit(.*)")
public @interface EnabledIfUnitTestsProfile
{
}

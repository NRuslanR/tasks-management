package edu.examples.todos.common.config.profiles;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Profile({ "default", "integration" })
public @interface DefaultProfile
{
}

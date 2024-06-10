package edu.examples.todos.common.config;

import edu.examples.todos.common.config.profiles.EnabledIfIntegrationTestsProfile;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnabledIfIntegrationTestsProfile
public @interface IntegrationTest
{
}

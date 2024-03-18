package edu.examples.todos.common.config;

import edu.examples.todos.common.config.profiles.EnabledIfUnitTestsProfile;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        ReactiveSecurityAutoConfiguration.class
})
@EnabledIfUnitTestsProfile
public @interface UnitTest
{
}

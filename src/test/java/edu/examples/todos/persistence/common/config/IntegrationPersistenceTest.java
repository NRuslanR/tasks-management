package edu.examples.todos.persistence.common.config;

import edu.examples.todos.common.config.IntegrationTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DataJpaTest
@TestPropertySources(
        @TestPropertySource(locations = "classpath:")
)
@IntegrationTest
public @interface IntegrationPersistenceTest
{
}

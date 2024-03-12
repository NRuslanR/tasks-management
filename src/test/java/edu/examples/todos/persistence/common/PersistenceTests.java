package edu.examples.todos.persistence.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

@DataJpaTest
@TestPropertySources(
        @TestPropertySource(locations = "classpath:")
)
public class PersistenceTests
{
}

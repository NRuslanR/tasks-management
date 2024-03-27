package edu.examples.todos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/*
    to-dos:
    1. Add reactive netty server support
    2. Create ResponseLocationHeaderFilter extends ResponseEntityResultHandler to set location header
        for responses of the created resources
    3. Add JOOQ support to CQRS JdbcToDoAccountingQueryUseCases class
    4. Add RSQL support related with JOOQ to implement the data filtering via http REST API
    5. Add Author for To-Do and related access rights' domain logic
    6. Add entity per To-Do domain object's life cycle stage
    7. Add domain services to figure out user's access rights and using it to form available hypermedia links
        in the controllers' returned resources
    7. Add standard userid/password-based authentication
    8. Add JWT token-based authentication
    9. controller's api-prefix value extract to application.yaml but WebFluxLinkBuilder doesn't resolve SpEL
    10. Add CQRS service method for getting of the full To-Do's info that is the including sub To-Dos

 */
@SpringBootApplication
@Slf4j
public class ToDosApplication implements CommandLineRunner
{
    public static void main(String[] args)
    {
        SpringApplication.run(ToDosApplication.class);
    }

    @Override
    public void run(String... args) throws Exception
    {
        log.info("CONVERTERS: " + StringUtils.collectionToCommaDelimitedString(Arrays.asList(args)));
    }

}

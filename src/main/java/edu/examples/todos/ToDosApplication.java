package edu.examples.todos;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/*
    to-dos:
    1. Create ResponseLocationHeaderFilter extends ResponseEntityResultHandler to set location header
        for responses of the created resources
    2. Add JOOQ support to CQRS JdbcToDoAccountingQueryUseCases class
    3. Add RSQL support related with JOOQ to implement the data filtering via http REST API
    4. Add entity per To-Do domain object's life cycle stage
    5. controller's api-prefix value extract to application.yaml but WebFluxLinkBuilder doesn't resolve SpEL
    6. Turn into to To-Dos microservice (
            API Gateway, two microservices - business and cqrs, Kafka, RabbitMQ,
            Spring Cloud dependencies, Consumer Driven Tests, Eventuate Tram framework,
            Transactional Outbox, Transactional log tailing and more and more
        )
    7. Add Swagger Open API Support
    8. Consider R2DBC using
 */

@SpringBootApplication(
//        exclude = {
//                SecurityAutoConfiguration.class,
//                UserDetailsServiceAutoConfiguration.class
//        }
)
@Slf4j
public class ToDosApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ToDosApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("CONVERTERS: " + StringUtils.collectionToCommaDelimitedString(Arrays.asList(args)));
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory()
    {
        return new NettyReactiveWebServerFactory();
    }
}

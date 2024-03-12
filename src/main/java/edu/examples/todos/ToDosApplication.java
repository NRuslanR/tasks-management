package edu.examples.todos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Stream;

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
        log.info(Stream.of(args).reduce("", (s, s2) -> s + ", " + s2));
    }
}

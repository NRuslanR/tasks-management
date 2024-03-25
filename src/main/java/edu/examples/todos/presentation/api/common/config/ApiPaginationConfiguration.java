package edu.examples.todos.presentation.api.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApiPaginationConfiguration
{
    @Value("${web.api.pagination.default-page}")
    private int defaultPage;

    @Value("${web.api.pagination.default-page-size}")
    private int defaultPageSize;
}

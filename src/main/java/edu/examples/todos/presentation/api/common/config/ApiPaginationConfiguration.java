package edu.examples.todos.presentation.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;

import lombok.Data;

@Data
@Configuration
public class ApiPaginationConfiguration
{
    @Value("${web.api.pagination.default-page}")
    private int defaultPage;

    @Value("${web.api.pagination.default-page-size}")
    private int defaultPageSize;

    @Bean
    public PagedResourcesAssembler<?> pagedResourcesAssembler() 
    {
        return new PagedResourcesAssembler<Object>(pageableResolver(), null);
    }

    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() 
    {
        return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
    }

    @Bean
    public HateoasSortHandlerMethodArgumentResolver sortResolver() 
    {
        return new HateoasSortHandlerMethodArgumentResolver();
    }
}

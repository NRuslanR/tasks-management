package edu.examples.todos.presentation.api.security.config;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.examples.todos.domain.resources.users.UserClaims;
import edu.examples.todos.domain.resources.users.UserId;
import edu.examples.todos.domain.resources.users.UserName;
import edu.examples.todos.persistence.repositories.users.UserRepository;
import edu.examples.todos.presentation.api.security.authentication.customizers.AuthenticationBuilderCustomizer;
import edu.examples.todos.presentation.api.security.authentication.customizers.HttpBasicAuthenticationBuilderCustomizer;
import edu.examples.todos.presentation.api.security.authentication.customizers.JwtAuthenticationBuilderCustomizer;
import edu.examples.todos.presentation.api.security.authentication.exception_handling.ApiAccessDeniedHandler;
import edu.examples.todos.presentation.api.security.authentication.exception_handling.ApiAuthenticationEntryPoint;
import edu.examples.todos.presentation.api.security.authentication.filters.JwtAuthenticationFilter;
import edu.examples.todos.presentation.api.security.authentication.filters.JwtAuthorizationHeaderSetFilter;
import edu.examples.todos.presentation.api.security.services.clients.ClientAuthority;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsRepository;
import edu.examples.todos.presentation.api.security.services.clients.StandardClientDetailsService;
import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import lombok.SneakyThrows;

@Configuration
@EnableMethodSecurity(
        jsr250Enabled = true,
        prePostEnabled = true,
        securedEnabled = true
)
@EnableWebFluxSecurity
public class ApiSecurityConfig
{
    @Autowired
    private ObjectMapper jacksonMapper;

    @HttpBasicRealmValue
    private String realmName;

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Autowired
    private RequestedContentTypeResolver requestedContentTypeResolver;

    @SneakyThrows
    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity httpSecurity,
            AuthenticationBuilderCustomizer authenticationBuilderCustomizer
    )
    {
        httpSecurity
                .csrf(c -> c.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .exceptionHandling(c ->
                        c
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .headers(c -> c.frameOptions(v -> v.disable()))
                .authorizeExchange(
                        c ->
                            c
                            .pathMatchers("/api/todos/**").hasRole("USER")
                            .pathMatchers("/api/users/**").hasRole("ADMIN")
                            .pathMatchers("/api/clients/**").permitAll()
                            .pathMatchers("/h2-dev").permitAll()
                            .anyExchange().authenticated()
                );

        authenticationBuilderCustomizer.customizeAuthenticationBuilder(httpSecurity);

        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "basic")
    public HttpBasicAuthenticationBuilderCustomizer httpBasicAuthenticationBuilderCustomizer()
    {
        return new HttpBasicAuthenticationBuilderCustomizer();
    }

    @Bean
    @ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "jwt")
    public JwtAuthenticationBuilderCustomizer jwtAuthenticationBuilderCustomizer(
            JwtService jwtService, ReactiveUserDetailsService userDetailsService
    )
    {
        return new JwtAuthenticationBuilderCustomizer(jwtAuthenticationFilter(jwtService, userDetailsService));
    }

    @Bean
    @ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "jwt")
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, ReactiveUserDetailsService userDetailsService)
    {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "jwt")
    @Bean
    public JwtAuthorizationHeaderSetFilter jwtAuthorizationHeaderSetFilter()
    {
        return new JwtAuthorizationHeaderSetFilter(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint()
    {
        return new ApiAuthenticationEntryPoint(jacksonMapper, realmName);
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler()
    {
        return new ApiAccessDeniedHandler(jacksonMapper);
    }

    @Bean
    public StandardClientDetailsService userDetailsService(
            ClientDetailsRepository clientDetailsRepository,
            UserRepository userRepository,
            @Value("${application.security.authentication.admin.id:admin}") String adminId,
            @Value("${application.security.authentication.admin.secret:admin}") String adminSecret,
            @Value("${application.security.authentication.admin.first-name:Ruslan}") String adminFirstName,
            @Value("${application.security.authentication.admin.last-name:Nigmatullin}") String adminLastName
    )
    {
        var clientDetailsService = new StandardClientDetailsService(clientDetailsRepository);

        var adminUser =
                edu.examples.todos.domain.resources.users.User.of(
                        UserId.of(UUID.randomUUID()),
                        UserName.of(adminFirstName, adminLastName),
                        UserClaims.unbounded()
                );

        adminUser = userRepository.save(adminUser);

        clientDetailsService.createClientDetails(
                ClientDetails.of(
                        adminId,
                        passwordEncoder().encode(adminSecret),
                        adminUser.getId(),
                        Set.of(ClientAuthority.of("ADMIN"))
                )
        ).block();

        return clientDetailsService;
    }

    @SneakyThrows
    @Bean
    public ReactiveAuthenticationManager authenticationManager(
        ReactiveUserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    )
    {
        var authenticationManager =
            new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;
    }

    @Bean
    @ConditionalOnExpression("false")
    public UserDetailsService inMemoryUserDetailsService()
    {
        var admin =
                User
                    .withUsername("admin")
                    .password(passwordEncoder().encode("123"))
                    .roles("ADMIN")
                    .build();

        var user =
                User
                    .withUsername("user")
                    .password(passwordEncoder().encode("123"))
                    .roles("USER")
                    .build();

                    
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public RoleHierarchy roleHierarchy()
    {
        var roleHierarchy = 
            RoleHierarchyImpl
                .fromHierarchy("ROLE_ADMIN > ROLE_USER \n");

        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

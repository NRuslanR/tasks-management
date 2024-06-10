package edu.examples.todos.presentation.api.security.config;

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
import edu.examples.todos.presentation.api.security.services.clients.ClientAuthority;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetails;
import edu.examples.todos.presentation.api.security.services.clients.ClientDetailsRepository;
import edu.examples.todos.presentation.api.security.services.clients.StandardClientDetailsService;
import edu.examples.todos.presentation.api.security.services.jwt.JwtService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Configuration
@EnableMethodSecurity(
        jsr250Enabled = true,
        prePostEnabled = true,
        securedEnabled = true
)
@EnableWebSecurity
public class ApiSecurityConfig
{
    @Autowired
    private ObjectMapper jacksonMapper;

    @HttpBasicRealmValue
    private String realmName;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            HandlerMappingIntrospector introspector,
            AuthenticationBuilderCustomizer authenticationBuilderCustomizer
    )
    {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        httpSecurity
                .csrf(c -> c.disable())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .exceptionHandling(c ->
                        c
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .headers(c -> c.frameOptions(v -> v.disable()))
                .authorizeHttpRequests(
                        c ->
                            c
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/todos/**")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/users/**")).hasRole("ADMIN")
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/clients/**")).permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().authenticated()
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
            JwtService jwtService, UserDetailsService userDetailsService
    )
    {
        return new JwtAuthenticationBuilderCustomizer(jwtAuthenticationFilter(jwtService, userDetailsService));
    }

    @Bean
    @ConditionalOnProperty(name = "application.security.authentication.methods.active", havingValue = "jwt")
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService)
    {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
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
    public AuthenticationEntryPoint authenticationEntryPoint()
    {
        return new ApiAuthenticationEntryPoint(jacksonMapper, realmName);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler()
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
        );

        return clientDetailsService;
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    {
        return authenticationConfiguration.getAuthenticationManager();
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
        var roleHierarchy = new RoleHierarchyImpl();

        roleHierarchy.setHierarchy(
                "ROLE_ADMIN > ROLE_USER \n"
        );

        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

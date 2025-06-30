package com.example.demo.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    /**
     * Configures the security filter chain.
     * - Disables CSRF for simplicity (stateless API).
     * - Requires basic authentication only for Swagger endpoints.
     * - Permits all other requests.
     * - Uses stateless session management.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                )
              ;

        return http.build();
    }

    /**
     * Configures an in-memory user with credentials from application properties.
     * NOTE: `withDefaultPasswordEncoder` is insecure and should not be used in production.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername(username)
                .password("{noop}" + password) // {noop} means no encoding
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}

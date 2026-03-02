package com.victor.bootcampproject.security;

import com.victor.bootcampproject.security.filters.CustomAuthenticationFilter;
import com.victor.bootcampproject.security.filters.CustomAuthorizationFilterLocal;
import com.victor.bootcampproject.service.UserServiceLocal;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * This is the main configuration class for security in the application. It enables web security,
 * sets up the password encoder, and sets up the security filter chain.
 */
@Configuration
@EnableWebSecurity
@Profile("dev-MySQL")
public class SecurityConfigLocal extends SecurityConfig {
    private final UserServiceLocal userService;

    public SecurityConfigLocal(AuthenticationManagerBuilder authManagerBuilder, UserServiceLocal userService) {
        super(authManagerBuilder);
        this.userService = userService;
    }

    /**  Bean definition for PasswordEncoder
     *
     * @return an instance of the DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    /**  Bean definition for SecurityFilterChain
     *
     * @param http the instance of HttpSecurity
     * @return an instance of the SecurityFilterChain
     * @throws Exception if there is an issue building the SecurityFilterChain
     */
    @Bean
    protected SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        // CustomAuthenticationFilter instance created
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManagerBuilder.getOrBuild());
        // set the URL that the filter should process
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        // disable CSRF protection
        http.csrf(csrf -> csrf.disable());

        http.cors(Customizer.withDefaults());  // CORS aktivieren

        http.headers(headers -> headers.frameOptions().disable()); // for H2-console
        // set the session creation policy to stateless
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        // set up authorization for different request matchers and user roles
        // modify this to have different configurations
        http.authorizeHttpRequests((requests) -> requests

                .requestMatchers("/").permitAll()   //for web

                .requestMatchers("/types").permitAll()
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/**", "index.html", "/static/**", "/assets/**").permitAll()

                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/login/**").permitAll()
                .requestMatchers("/api/admin/users").hasAnyAuthority("ROLE_ADMIN")

                .requestMatchers(GET, "/api/users/me").permitAll()
                .requestMatchers(POST, "/api/users").permitAll()
                .requestMatchers(PATCH, "/api/users").permitAll()

                .requestMatchers(GET,
                        "/api/users", "/api/todolist/**").hasAnyAuthority("ROLE_USER")
                .requestMatchers(PATCH, "/api/users").hasAnyAuthority("ROLE_USER")

                .requestMatchers(POST, "/api/todolist/**").hasAnyAuthority("ROLE_USER")
                .requestMatchers(PATCH, "/api/todolist/**").hasAnyAuthority("ROLE_USER")
                .requestMatchers(DELETE, "/api/todolist/**").hasAnyAuthority("ROLE_USER")


                .anyRequest().authenticated());
        // add the custom authentication filter to the http security object
        http.addFilter(customAuthenticationFilter);
        // Add the custom authorization filter before the standard authentication filter.
        http.addFilterBefore(new CustomAuthorizationFilterLocal(userService), UsernamePasswordAuthenticationFilter.class);

        // Build the security filter chain to be returned.
        return http.build();
    }
}

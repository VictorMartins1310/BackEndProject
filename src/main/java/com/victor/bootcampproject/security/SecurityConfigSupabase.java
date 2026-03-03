package com.victor.bootcampproject.security;

import com.victor.bootcampproject.security.filters.CustomAuthorizationFilterSupaBase;
import com.victor.bootcampproject.service.UserServiceSupaBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import org.springframework.context.annotation.Profile;

/**
 * This is the main configuration class for security in the application. It enables web security,
 * sets up the password encoder, and sets up the security filter chain.
 */
@Configuration
@EnableWebSecurity
@Profile("dev-SupaBase")
public class SecurityConfigSupabase extends SecurityConfig {

    private final UserServiceSupaBase userService;

    public SecurityConfigSupabase(AuthenticationManagerBuilder authManagerBuilder, UserServiceSupaBase userService) {
        super(authManagerBuilder);
        this.userService = userService;
    }

    @Bean
    public CustomAuthorizationFilterSupaBase customAuthorizationFilter(UserServiceSupaBase userService) {
        return new CustomAuthorizationFilterSupaBase(userService);
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
    protected SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        // disable CSRF protection
        http.csrf(csrf -> csrf.disable());

        http.cors(Customizer.withDefaults());  // CORS aktivieren

        http.headers(headers -> headers.frameOptions().disable()); // for H2-console
        // set the session creation policy to stateless
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        // set up authorization for different request matchers and user roles
        // modify this to have different configurations
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(listOfPermitAll).permitAll()
                .requestMatchers("/api/admin/users").hasAnyAuthority("ROLE_ADMIN")

                .requestMatchers(GET, listOfPermitAll).permitAll()
                .requestMatchers(POST, listOfPermitAll).permitAll()
                .requestMatchers(PATCH, listOfPermitAll).permitAll()

                .requestMatchers(GET, listOfUser).hasAnyAuthority("ROLE_USER")
                .requestMatchers(PATCH, listOfUser).hasAnyAuthority("ROLE_USER")

                .requestMatchers(POST, listOfUser).hasAnyAuthority("ROLE_USER")
                .requestMatchers(PATCH, listOfUser).hasAnyAuthority("ROLE_USER")
                .requestMatchers(DELETE, listOfUser).hasAnyAuthority("ROLE_USER")

                .anyRequest().authenticated());
        // Add the custom authorization filter before the standard authentication filter.

        http.addFilterBefore(customAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter.class);


        // Build the security filter chain to be returned.
        return http.build();
    }
}
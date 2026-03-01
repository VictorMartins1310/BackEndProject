package com.bootcamp.project.security;

import java.util.List;
import com.bootcamp.project.security.filters.CustomAuthenticationFilter;
import com.bootcamp.project.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    /**
     * This is the main configuration class for security in the application. It enables web security,
     * sets up the password encoder, and sets up the security filter chain.
     */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public abstract class SecurityConfig {
    // Instance of the AuthenticationManagerBuilder
    private final AuthenticationManagerBuilder authManagerBuilder;

    /**  Bean definition for PasswordEncoder
     *
     * @return an instance of the DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder encoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    /**
         * Bean definition for AuthenticationManager
         *
         * @param authenticationConfiguration the instance of AuthenticationConfiguration
         * @return an instance of the AuthenticationManager
         * @throws Exception if there is an issue getting the instance of the AuthenticationManager
         */
    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**  Bean definition for SecurityFilterChain
     *
     * @param http the instance of HttpSecurity
     * @return an instance of the SecurityFilterChain
     * @throws Exception if there is an issue building the SecurityFilterChain
     */
    @Bean
    protected SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
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
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build the security filter chain to be returned.
        return http.build();
    }

    @Profile({"dev"})
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://192.168.178.253:5173",
                "http://msi:5173",

                "http://localhost:8710",
                "http://192.168.178.253:8710",
                "http://msi:8710"));

        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // allow cookies/tokens

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
    }
}
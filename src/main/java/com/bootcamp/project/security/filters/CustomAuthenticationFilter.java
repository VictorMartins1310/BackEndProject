package com.bootcamp.project.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for CustomAuthenticationFilter
     *
     * @param authenticationManager
     */
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Attempts to authenticate the user with given credentials
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Authentication object if successful, otherwise throws an exception
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Using ObjectMapper to extract credentials from request body
        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);

            String email = credentials.get("email");
            String password = credentials.get("password");

            log.info("Username is: {}", email);
            log.info("Password is: {}", password);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            log.error("Error reading credentials from request body", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Method is called if the user is successfully authenticated
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param chain          FilterChain
     * @param authentication Authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // Cast the authentication principal to User object
        User user = (User) authentication.getPrincipal();
        // Creating an HMAC256 encoded JWT with secret key
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        // Adding user details and roles to the token
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // for generating a 2 hours valid Token
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        // Creating a map with the generated token
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        // Setting the response type to application/json
        response.setContentType(APPLICATION_JSON_VALUE);
        // Writing the token as response
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}

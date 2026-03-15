package com.victor.bootcampproject.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.UserServiceSupaBase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * CustomAuthorizationFilter is an implementation of OncePerRequestFilter to handle
 * authorization of a user to access the API endpoints.
 */
@Slf4j
@Profile({"dev-SupaBase", "prod"})
public class CustomAuthorizationFilterSupaBase extends CustomAuthorizationFilter {
    private final UserServiceSupaBase userService;

    public CustomAuthorizationFilterSupaBase(UserServiceSupaBase userService) {
        this.userService = userService;
    }

    /**
     * The method doFilterInternal will handle the authorization of a user to access the API endpoints.
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException if there is a servlet related error
     * @throws IOException      if there is an Input/Output error
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

  /*      if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }*/
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        System.out.println("AUTHORIZATION HEADER" + request.getHeader(AUTHORIZATION));

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());

                DecodedJWT decoded = JWT.decode(token);
                String kid = decoded.getKeyId();

                ECPublicKey publicKey = loadPublicKey(kid);
                Algorithm algorithm = getAlgorithm(token);

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(token);


                // 4. Claims auslesen
                String userId = jwt.getSubject();
                String email = jwt.getClaim("email").asString();
                String sub =  jwt.getClaim("sub").asString();

                // 5. User synchronisieren
                AppUser appUser = userService.syncUserFromSupabase(UUID.fromString(userId), email);

                // 6. Authorities setzen
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                appUser.getRoles().forEach(role ->
                        authorities.add(new SimpleGrantedAuthority(role.getRole()))
                );

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(appUser, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
                return;

            } catch (Exception e) {
                log.error("Error logging in: {}", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("error_message", e.getMessage()));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

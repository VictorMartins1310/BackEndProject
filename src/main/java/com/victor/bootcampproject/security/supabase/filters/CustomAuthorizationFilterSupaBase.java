package com.victor.bootcampproject.security.supabase.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.security.SupabaseConfig;
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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
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
@Profile({"dev-SupaBase"})
public class CustomAuthorizationFilterSupaBase extends com.victor.bootcampproject.security.filters.CustomAuthorizationFilter {

    private final UserServiceSupaBase userService;
    private SupabaseConfig supabaseConfig = new SupabaseConfig();

    public CustomAuthorizationFilterSupaBase(UserServiceSupaBase userService) {
        this.userService = userService;
    }

    private ECPublicKey loadPublicKey(String kid) throws Exception {
        URL url = new URL( SupabaseConfig.SUPABASE_URL+ "/auth/v1/.well-known/jwks.json");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("JWKS request failed with status " + status);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jwks = mapper.readTree(connection.getInputStream());

        for (JsonNode key : jwks.get("keys")) {
            if (key.get("kid").asText().equals(kid)) {

                String x = key.get("x").asText();
                String y = key.get("y").asText();
                String crv = key.get("crv").asText();

                String javaCurveName = "secp256r1";

                byte[] xBytes = Base64.getUrlDecoder().decode(x);
                byte[] yBytes = Base64.getUrlDecoder().decode(y);

                ECPoint ecPoint = new ECPoint(
                        new BigInteger(1, xBytes),
                        new BigInteger(1, yBytes)
                );

                AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
                parameters.init(new ECGenParameterSpec(javaCurveName));
                ECParameterSpec ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);

                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                return (ECPublicKey) keyFactory.generatePublic(
                        new ECPublicKeySpec(ecPoint, ecParameterSpec)
                );
            }
        }

        throw new RuntimeException("No matching EC key found for kid " + kid);
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
                Algorithm algorithm = Algorithm.ECDSA256(publicKey, null);

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(token);


                // 4. Claims auslesen
                String userId = jwt.getSubject();
                String email = jwt.getClaim("email").asString();
                String sub =  jwt.getClaim("sub").asString();


                System.out.println("Email: " + email);
                System.out.println("Sub: " + sub);
                System.out.println("USER ID: " + userId);


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

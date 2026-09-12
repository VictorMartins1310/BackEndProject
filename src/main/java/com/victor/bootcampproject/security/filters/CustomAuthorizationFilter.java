package com.victor.bootcampproject.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.bootcampproject.exception.ProjectException;
import com.victor.bootcampproject.security.SupabaseConfig;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.StringReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Base64;

public abstract class CustomAuthorizationFilter extends OncePerRequestFilter {
    protected final SupabaseConfig dbConfig;

    public CustomAuthorizationFilter(SupabaseConfig supabaseConfig) {
        this.dbConfig = supabaseConfig;
    }

    // Todo -> JavaDoc for this function
    protected ECPublicKey loadPublicKey(String kid, String connectionURL) throws Exception {
        URL url = new URL(connectionURL);
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

    /** Function to get the algorithm from Token
     * @param token
     * @return
     */
    protected Algorithm getAlgorithm(@NonNull String token, String connectionURL) throws Exception {
        String algorithmType = "";
        if (token.split("\\.").length == 3) {
            String headerJson = new String(Base64.getUrlDecoder().decode(token.split("\\.")[0]));
            JsonObject header = Json.createReader(new StringReader(headerJson)).readObject();
            algorithmType = header.getString("alg");
        }
        if (algorithmType.isBlank())
            throw new ProjectException("Invalid algorithm type");
        else {
            switch (algorithmType) {
                case "HS256", "RS256":          //MySQL
                    return Algorithm.HMAC256("secret".getBytes());
                case "ES256":                   //Supabase
                    DecodedJWT decoded = JWT.decode(token);
                    String kid = decoded.getKeyId();
                    ECPublicKey publicKey = loadPublicKey(kid, dbConfig.SUPABASE_JWKS_URl);
                    return Algorithm.ECDSA256(publicKey, null);
                case "HS384":
                    return Algorithm.HMAC384("secret".getBytes());
                default:
                    return Algorithm.HMAC512("secret".getBytes());
            }
        }
    }
}

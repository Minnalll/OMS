package com.oms.auth.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import com.oms.auth.model.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.HashMap;
import io.jsonwebtoken.SignatureAlgorithm;

import com.oms.auth.properties.JwtProperties;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

import com.oms.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    private final Key secretKey;
    private final JwtProperties jwtProperties;

    public JwtService(Key secretKey,
                      JwtProperties jwtProperties) {

        this.secretKey = secretKey;
        this.jwtProperties = jwtProperties;

    }

    public String generateAccessToken(String username) {

        return Jwts.builder()

                .subject(username)

                .issuedAt(new Date())

                .expiration(new Date(

                        System.currentTimeMillis()

                                + jwtProperties.getAccessTokenExpiration()

                ))

                .signWith(secretKey, SignatureAlgorithm.HS512)

                .compact();

    }
    public String generateAccessToken(
            String username,
            Map<String, Object> claims) {

        return Jwts.builder()

                .claims(claims)

                .subject(username)

                .issuedAt(new Date())

                .expiration(new Date(

                        System.currentTimeMillis()

                                + jwtProperties.getAccessTokenExpiration()

                ))

                .signWith(secretKey, SignatureAlgorithm.HS512)

                .compact();

    }
    public String generateRefreshToken(String username) {

        return Jwts.builder()

                .subject(username)

                .issuedAt(new Date())

                .expiration(new Date(

                        System.currentTimeMillis()

                                + jwtProperties.getRefreshTokenExpiration()

                ))

                .signWith(secretKey, SignatureAlgorithm.HS512)

                .compact();

    }
    public String extractUsername(String token) {

        return extractClaim(

                token,

                Claims::getSubject

        );

    }
    public <T> T extractClaim(

            String token,

            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith((javax.crypto.SecretKey) secretKey)

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }
    public Date extractExpiration(String token) {

        return extractClaim(

                token,

                Claims::getExpiration

        );

    }
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)

                .before(new Date());

    }
    public boolean validateToken(

            String token,

            String username) {

        String extractedUser =

                extractUsername(token);

        return extractedUser.equals(username)

                && !isTokenExpired(token);

    }

    public String generateAccessToken(
            User user) {

        Map<String,Object> claims = new HashMap<>();

        claims.put("role", user.getRole().name());

        claims.put("email", user.getEmail());

        return generateAccessToken(
                user.getUsername(),
                claims);
    }

    public Claims extractClaims(String token) {

        return Jwts.parser()

                .verifyWith((SecretKey) secretKey)

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

    public String extractRole(String token) {

        Claims claims = extractClaims(token);

        return claims.get("role", String.class);

    }

    public boolean isValidToken(String token) {

        try {

            extractClaims(token);

            return !isTokenExpired(token);

        }

        catch (Exception ex) {

            return false;

        }

    }



}
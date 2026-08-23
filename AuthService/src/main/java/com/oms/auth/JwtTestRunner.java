/*package com.oms.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.oms.auth.properties.JwtProperties;

@Component
public class JwtTestRunner implements CommandLineRunner {

    private final JwtProperties jwtProperties;

    public JwtTestRunner(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void run(String... args) {

        System.out.println(jwtProperties.getSecret());

        System.out.println(jwtProperties.getAccessTokenExpiration());

        System.out.println(jwtProperties.getRefreshTokenExpiration());

    }

}*/
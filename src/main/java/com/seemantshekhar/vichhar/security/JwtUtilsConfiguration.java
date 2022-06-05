package com.seemantshekhar.vichhar.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtilsConfiguration {

    @Bean
    public JwtUtils getJwtUtils(
            @Value("${vichaar.auth.token.sign-key}") String signKey,
            @Value("${vichaar.auth.token.valid-time}") Long validTime) throws Exception {
        if(signKey.length() < 32){
            throw  new Exception("sign key length can't be less than 32");
        }
        return new JwtUtils(signKey, validTime);
    }

}

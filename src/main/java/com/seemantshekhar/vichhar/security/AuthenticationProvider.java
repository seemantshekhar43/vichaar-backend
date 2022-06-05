package com.seemantshekhar.vichhar.security;

import com.seemantshekhar.vichhar.beans.user.UserDto;
import com.seemantshekhar.vichhar.beans.user.UserEntity;
import com.seemantshekhar.vichhar.service.userservice.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {
    private final UserDetailsServiceImpl userDetailsService;

    public Authentication getAuthentication(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null){
            return null;
        }
        UserEntity userEntity =(UserEntity) userDetails;
        UserDto.Auth authenticatedUser =  UserDto.Auth.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .build();
        return  new UsernamePasswordAuthenticationToken(authenticatedUser, "", userDetails.getAuthorities());
    }
}

package com.example.demo.security;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.services.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthedManager implements AuthenticationManager {
    private final UserDetailService detailsService;
    private final Logger logger = LoggerFactory.getLogger(AuthedManager.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("credentials: " + authentication.getCredentials());
        logger.info("principals: " + authentication.getPrincipal());
        if (authentication.getCredentials() == null || authentication.getPrincipal() == null) {
            throw new BadCredentialsException("Credentials are wrong");
        }
        CustomUserDetails user = loadUser(authentication);
        if (user == null) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }
    private CustomUserDetails loadUser(Authentication auth) {
        return detailsService.loadUserByUsername(auth.getPrincipal().toString());
    }


}

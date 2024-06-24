package com.example.demo.security;

import com.example.demo.customModels.CustomUser;
import com.example.demo.services.UserService;
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
public class AuthManager implements AuthenticationManager {
    private final UserService detailsService;
    private final Logger logger = LoggerFactory.getLogger(AuthManager.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("credentials: " + authentication.getCredentials());
        logger.info("principals: " + authentication.getPrincipal());
        if (authentication.getCredentials() == null || authentication.getPrincipal() == null) {
            throw new BadCredentialsException("Credentials are wrong");
        }
        CustomUser user = loadUser(authentication);
        if (user == null) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }
    private CustomUser loadUser(Authentication auth) {
        return detailsService.loadUserByUsername(auth.getPrincipal().toString());
    }
}

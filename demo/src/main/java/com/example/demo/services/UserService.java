package com.example.demo.services;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.ResponseHandler;
import com.example.demo.security.AuthedManager;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private AuthedManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;



    public ResponseEntity<Object> authenticateUser(AuthRequest authRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            var userDetails = userRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));
            CustomUserDetails customUserDetails =new CustomUserDetails();
            customUserDetails.setUser(userDetails);
            JwtService jwtService=new JwtService();
            String role = authRequest.getRole().name();
            if (!role.equals("ROLE_USER")) {
                return ResponseEntity.status(403).body("Access Denied");
            }
            var jwt=jwtService.generateToken(customUserDetails);
            return  ResponseHandler.responseBuilder("Auth success", HttpStatus.OK,new AuthResponse(jwt));
        } catch (UsernameNotFoundException ex) {
            // Handle UsernameNotFoundException
            return ResponseHandler.responseBuilder("Username not found", HttpStatus.BAD_REQUEST, null);
        } catch (AuthenticationException ex) {
            // Handle AuthenticationException
            return ResponseHandler.responseBuilder("Authentication failed", HttpStatus.UNAUTHORIZED, null);
        }
    }
        public ResponseEntity<Object> registerUser(AuthRequest authRequest) {
        CustomUserDetails customUser=new CustomUserDetails();
        User user=new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(authRequest.getRole());
        customUser.setUser(user);
        boolean userDetails = userRepository.existsByUsername(authRequest.getUsername());
        if (!userDetails) {
            userRepository.save(user);
            var jwt=jwtService.generateToken(customUser);
            return  ResponseHandler.responseBuilder("registerUser success", HttpStatus.OK,new AuthResponse(jwt));
        }
        return  ResponseHandler.responseBuilder("This username is Existed", HttpStatus.BAD_REQUEST,null);
    }
}

package com.example.demo.controllers;

import com.example.demo.customModels.CustomUser;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.ResponseHandler;
import com.example.demo.security.AuthManager;
import com.example.demo.security.JwtService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
public class UserController {
    @Autowired
    AuthManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is exception");
    }
//    @PostMapping("/register")
//    public ResponseEntity<Object> register(@RequestBody AuthRequest request) {
//        return userService.registerUser(request);
//    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody AuthRequest authRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        var userDetails = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));
        CustomUser customUser=new CustomUser();
        customUser.setUser(userDetails);
         JwtService jwtService=new JwtService();
        var jwt=jwtService.generateToken(customUser);
        return  ResponseHandler.responseBuilder("Auth success", HttpStatus.OK,new AuthResponse(jwt));
        } catch (UsernameNotFoundException ex) {
            // Handle UsernameNotFoundException
            return ResponseHandler.responseBuilder("Username not found", HttpStatus.BAD_REQUEST, null);
        } catch (AuthenticationException ex) {
            // Handle AuthenticationException
            return ResponseHandler.responseBuilder("Authentication failed", HttpStatus.UNAUTHORIZED, null);
        }

    }



}

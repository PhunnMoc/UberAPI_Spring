package com.example.demo.controllers;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.ResponseHandler;
import com.example.demo.security.AuthedManager;
import com.example.demo.security.JwtService;
import com.example.demo.services.UserDetailService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
public class UserController {
    @Autowired
    AuthedManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is exception");
    }
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid AuthRequest request, BindingResult bindingResult) {
        return userService.registerUser(request,bindingResult);
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
        return userService.authenticateUser(authRequest,bindingResult);
    }



}

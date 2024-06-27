package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.handlers.ResponseHandler;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.AuthedManager;
import com.example.demo.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
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
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        return userService.registerUser(request,bindingResult);
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
        return userService.authenticateUser(authRequest,bindingResult);
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
           // return  ResponseHandler.responseBuilder("This username is Existed", HttpStatus.O,null);
            return "verified";
        } else {
          //  model.addAttribute("message", "Invalid verification token.");
            return "verify-email";
        }
    }




}

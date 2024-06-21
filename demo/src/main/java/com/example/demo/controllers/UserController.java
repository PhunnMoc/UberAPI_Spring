package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.response.ResponseHandler;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthRequest request) {

        return userService.registerUser(request);
    }

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is exception");
    }

}

package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/driver")
public class DriverController {
    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is exception");
    }
}

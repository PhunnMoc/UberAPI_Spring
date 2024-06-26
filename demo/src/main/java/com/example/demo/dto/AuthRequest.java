package com.example.demo.dto;

import com.example.demo.customModels.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Size(max=20)
    @NotEmpty(message = "You must enter your username")
    private String username;
    @Size(min=6, max = 20)
    @NotEmpty(message = "You must enter your password")
    private String password;
    private Role role;
}

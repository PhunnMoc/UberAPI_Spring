package com.example.demo.models;

import com.example.demo.customModels.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.rmi.server.UID;
@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    private String id;

    @Size(max=20)
    @NotEmpty(message = "You must enter your username")
    private String username;

    @Size(min=6, max = 20)
    @NotEmpty(message = "You must enter your password")
    private String password;
    private Role role;
}

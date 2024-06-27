package com.example.demo.services;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.handlers.EmailValid;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.handlers.ResponseHandler;
import com.example.demo.security.AuthedManager;
import com.example.demo.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private AuthedManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private EmailValid emailValid;

    @Autowired
    private EmailSenderServices senderService;



    public ResponseEntity<Object> authenticateUser(@Valid AuthRequest authRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseHandler.responseBuilder(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST, null);
        }
        try {
            //spring security check auth
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            //get user from username
            var userDetails = userRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));

            //Create user details of this user
            CustomUserDetails customUserDetails =new CustomUserDetails();
            customUserDetails.setUser(userDetails);

            //Check if this account is verified
            if(!customUserDetails.isEnabled()){
                return ResponseHandler.responseBuilder("You must verify this account", HttpStatus.UNAUTHORIZED, null);
            }
            JwtService jwtService=new JwtService();

            //Check role if it valid
            String role = authRequest.getRole().name();
            Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
            boolean isAuthoz = authorities.stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));

            // Now you can use isAdmin to check if the user has the ADMIN role
            if (!isAuthoz) {
                return ResponseHandler.responseBuilder("Check your ROLE", HttpStatus.BAD_REQUEST, null);
            }

            //return token
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

    public ResponseEntity<Object> registerUser(@Valid RegisterRequest registRequest, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
       //Check if all value registRequest is valid
        if (bindingResult.hasErrors()) {
            return ResponseHandler.responseBuilder(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST, null);
        }
        //Create new user detail
        CustomUserDetails customUser=new CustomUserDetails();
        User user=new User();
        user.setUsername(registRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registRequest.getPassword()));
        user.setRole(registRequest.getRole());
        user.setName(registRequest.getName());
        user.setNumber(registRequest.getNumber());
        user.setEnabled(false);
        // Generate verification token and send email
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        customUser.setUser(user);
        boolean userDetails = userRepository.existsByUsername(registRequest.getUsername());
        if (!userDetails) {
            userRepository.save(user);
            var jwt=jwtService.generateToken(customUser);
            String confirmationUrl = "http://localhost:5000/auth/verify-email?token=" + token;
            senderService.sendRegisterEmail(registRequest.getUsername(), "Email Verification", confirmationUrl);
            return  ResponseHandler.responseBuilder("registerUser success", HttpStatus.OK,new AuthResponse(jwt));
        }

        return  ResponseHandler.responseBuilder("This username is Existed", HttpStatus.UNAUTHORIZED,null);
    }

//    public RegisterRequest<Object> fogetPass(){
//
//    }



    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
    }
    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }



}

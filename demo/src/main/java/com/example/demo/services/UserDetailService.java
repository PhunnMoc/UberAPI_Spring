package com.example.demo.services;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.dto.AuthRequest;
import com.example.demo.handlers.EmailValid;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByUsername(username);
        if (user.isEmpty()  ) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    public String test(AuthRequest authRequest){
        return authRequest.toString();
    }


}

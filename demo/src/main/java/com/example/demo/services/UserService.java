package com.example.demo.services;

import com.example.demo.customModels.CustomUser;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.ResponseHandler;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByUsername(username);
        if (user.isEmpty()  ) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUser(user);
    }

//    public ResponseEntity<Object> registerUser(AuthRequest authRequest) {
//        CustomUser customUser=new CustomUser();
//        User user=new User();
//        user.setUsername(authRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
//        user.setRole(authRequest.getRole());
//        customUser.setUser(user);
//        boolean userDetails = userRepository.existsByUsername(authRequest.getUsername());
//        if (!userDetails) {
//            userRepository.save(user);
//            var jwt=jwtService.generateToken(customUser);
//            return  ResponseHandler.responseBuilder("registerUser success", HttpStatus.OK,new AuthResponse(jwt));
//        }
//        return  ResponseHandler.responseBuilder("This username is Existed", HttpStatus.BAD_REQUEST,null);
//    }
//
//    public ResponseEntity<Object> authenticateUser(AuthRequest authRequest) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authRequest.getUsername(),
//                            authRequest.getPassword()
//                    )
//            );
//            // Nếu xác thực thành công, trả về phản hồi thành công
//            return ResponseEntity.ok("Đăng nhập thành công");
//        } catch (AuthenticationException ex) {
//            // Xử lý ngoại lệ xác thực
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc mật khẩu không đúng");
//        }
////        var userDetails = userRepository.findByUsername(authRequest.getUsername())
////                .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));
////        CustomUser customUser=new CustomUser();
////        customUser.setUser(userDetails);
////        var jwt=jwtService.generateToken(customUser);
////        return  ResponseHandler.responseBuilder("Auth success", HttpStatus.OK,new AuthResponse(jwt));
//
//    }



    public String test(AuthRequest authRequest){
        return authRequest.toString();
    }


}

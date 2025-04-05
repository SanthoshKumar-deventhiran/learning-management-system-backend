package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Exceptions.PasswordIncorrectException;
import com.example.demosecuretwo.Exceptions.UserNotFoundException;
import com.example.demosecuretwo.Exceptions.UserAlreadyExistsException;
import com.example.demosecuretwo.Model.Users;
import com.example.demosecuretwo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsersService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    private BCryptPasswordEncoder bcryt = new BCryptPasswordEncoder(12);

    public Map<String, Object>  signup(Users user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
        throw new UserAlreadyExistsException("Username is already taken. Please choose another.");
    }

        if (userRepo.findByEmail(user.getEmail()) != null) {
        throw new UserAlreadyExistsException("Email is already registered. Try logging in instead.");
    }
        user.setPassword(bcryt.encode(user.getPassword()));
        user.setRole(Users.Role.User);
        userRepo.save(user);
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("message", "Signup successful. Welcome!");
        response.put("status", HttpStatus.CREATED.value()); // Status code 201

        return response;
   }

    public Map<String, Object> verify(Users user) {
        Users dbUser = userRepo.findByUsername(user.getUsername()).orElse(null);

        if (dbUser == null) {
            dbUser = userRepo.findByEmail(user.getEmail()); // Check by email too
        }

        if (dbUser == null) {
            throw new UserNotFoundException("No account found with this username or email.");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dbUser.getUsername(), user.getPassword())
            );

            // Generate JWT token
            String token = jwtService.generateToken(dbUser.getUsername());

            // Prepare response with user details
            Map<String, Object> response = new HashMap<>();
            response.put("userId", dbUser.getId());
            response.put("username", dbUser.getUsername());
            response.put("token", token);
            response.put("message", "Login successful");
            response.put("status", HttpStatus.OK.value());

            return response;

        } catch (AuthenticationException e) {
            throw new PasswordIncorrectException("Invalid password. If you forgot your password, try resetting it.");
        }
    }
    public Integer getUserIdByUsername(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return user.getId();
    }
}

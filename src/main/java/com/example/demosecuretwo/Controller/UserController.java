package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.Model.Users;
import com.example.demosecuretwo.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UsersService usersService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello User";
    }

    @GetMapping("/welcome")
    public String Welcome(){
        return "welcome User";
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Users user) {
        Map<String, Object> response = usersService.signup(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>>login(@RequestBody Users users){
        return ResponseEntity.ok(usersService.verify(users));
    }
}

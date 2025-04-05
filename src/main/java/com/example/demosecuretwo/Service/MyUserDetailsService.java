package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Config.UserPrinciple;
import com.example.demosecuretwo.Model.Users;
import com.example.demosecuretwo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(identifier).orElse(null);

        if (user == null) {
            user = userRepo.findByEmail(identifier);
        }
        if(user==null){
            throw new UsernameNotFoundException("not found");
        }
        return new UserPrinciple(user);// UserDetails is also a interface so we want to create and implements the user details

    }
}
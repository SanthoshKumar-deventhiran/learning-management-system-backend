package com.example.demosecuretwo.Config;

import com.example.demosecuretwo.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UsersService userService;

    public Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      //  the principal represents the currently authenticated user. It contains the user's identity, which can be used to get details like username, roles, and permissions.
        if (principal instanceof UserDetails) {
            return userService.getUserIdByUsername(((UserDetails) principal).getUsername());
        }
        return null;
    }
}


package com.example.demosecuretwo.Config;

import com.example.demosecuretwo.Service.JWTService;
import com.example.demosecuretwo.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // skipping the bearer heading
            username = jwtService.extractUserName(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //SecurityContextHolder that acts as a container for the SecurityContext, which holds authentication-related details of the current user.
            // if is not null means that the user is already authenticated
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  //        UsernamePasswordAuthenticationToken is used to pass login credentials to the AuthenticationManager.
  //        and the infomation are stored in the securitycontextholder .After JWT Verification we just need to tell Spring Security.
  //        the user is already authenticated .the details is stored in SecurityContextHolder, so the user stays logged in.

                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

//                This adds extra details about the user's request (like IP address, session ID).
//                WebAuthenticationDetailsSource().buildDetails(request) extracts these details from the HttpServletRequest.
//                It helps with security logging and tracking user activity.
                SecurityContextHolder.getContext().setAuthentication(authToken);
//                It sets the authenticated user in the security context.
            }
        }

        filterChain.doFilter(request, response);
//        This line passes the request and response to the next filter in the chain
    }
}

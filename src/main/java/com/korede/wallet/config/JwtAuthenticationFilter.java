package com.korede.wallet.config;

import com.korede.wallet.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null && validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return jwtUtil.validateToken(token, username);
    }

}




//    private final JwtUtil jwtUtil;
//
//    @Autowired
//    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
////            throws ServletException, IOException {
////        String token = extractToken(request); // Extract the token from the request
////
////        if (token != null) {
////            System.out.println("Extracted token: " + token);
////
////            // Validate the token
////            boolean isValid = validateToken(token);
////            if (!isValid) {
////                System.out.println("Token validation failed.");
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token"); // Handle the failure (401 Unauthorized)
////                return; // Stop further processing
////            } else {
////                System.out.println("Token is valid.");
////                // Proceed with further processing, such as authentication or setting the security context
////            }
////        } else {
////            System.out.println("No token found.");
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided"); // Handle the case where no token is found
////            return; // Stop further processing
////        }
////
////        chain.doFilter(request, response); // Continue the filter chain
////    }
//
//    public boolean validateToken(String token) {
//        try {
//            final String username = jwtUtil.extractUsername(token); // Ensure this is correct
//            boolean isExpired = jwtUtil.isTokenExpired(token);
//            System.out.println("Username: " + username + ", Token expired: " + isExpired);
//            return (username != null && !isExpired);
//        } catch (SignatureException e) {
//            System.out.println("Invalid signature: " + e.getMessage());
//        } catch (MalformedJwtException e) {
//            System.out.println("Malformed token: " + e.getMessage());
//        } catch (ExpiredJwtException e) {
//            System.out.println("Token expired: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.out.println("Token is null or empty: " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("Token validation failed: " + e.getMessage());
//        }
//        return false;
//    }
//
////    private boolean isTokenExpired(String token) {
////        Date expiration =jwtUtil.extractAllClaims(token).getExpiration();
////        return expiration.before(new Date()); // Compare with current date
////    }
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain chain)
//            throws ServletException, IOException {
//        String token = extractToken(request);
//
//        if (token != null && jwtUtil.validateToken(token)) {
//            String username = jwtUtil.extractUsername(token);
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
////            throws ServletException, IOException {
////        String token = extractToken(request);
////
////        if (token != null && validateToken(token)) {
////            String username = jwtUtil.extractUsername(token);
////            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()); // Add authorities if needed
////            SecurityContextHolder.getContext().setAuthentication(authentication);
////        }
////
////        chain.doFilter(request, response);
////    }
//
//    private String extractToken(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (header != null && header.startsWith("Bearer ")) {
//            return header.substring(7); // Remove "Bearer " prefix
//        }
//        return null;
//    }
//
////    private boolean validateToken(String token) {
////        // You would retrieve the username from the context or request
////        String username = ""; // Retrieve from your context or request
////        return jwtUtil.validateToken(token, username);
////    }
////private boolean validateToken(String token) {
////    String username = jwtUtil.extractUsername(token); // Retrieve username from token
////    return jwtUtil.validateToken(token, username);
//////}
////public boolean validateToken(String token) {
////    try {
////        final String username = extractUsername(token);
////        boolean isExpired = isTokenExpired(token);
////        System.out.println("Username: " + username + ", Token expired: " + isExpired);
////        return (username != null && !isExpired);
////    } catch (Exception e) {
////        System.out.println("Token validation failed: " + e.getMessage());
////        return false;
////    }
////}
//}
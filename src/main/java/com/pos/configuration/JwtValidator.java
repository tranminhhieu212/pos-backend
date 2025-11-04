package com.pos.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            try {
                String token = jwt.substring(7);

                // Create SecretKey from your secret string
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET.getBytes());

                // Parse and validate JWT token - CORRECT SYNTAX FOR 0.12.5
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                // Extract email from claims
                String email = String.valueOf(claims.get("email"));

                // Extract authorities from claims
                String authorities = String.valueOf(claims.get("authorities"));

                // Create granted authorities list
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                if (authorities != null && !authorities.isEmpty() && !authorities.equals("null")) {
                    String[] authArray = authorities.split(",");
                    for (String auth : authArray) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(auth.trim()));
                    }
                }

                // Create authentication object
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        grantedAuthorities
                );

                // Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                System.err.println("Invalid JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
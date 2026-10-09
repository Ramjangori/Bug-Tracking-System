package com.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.impl.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return request.getServletPath().equals("/auth/login");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // No JWT cookie
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String email = jwtService.extractEmail(token);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            if (email.equals(userDetails.getUsername())
                    && jwtService.isTokenValid(token, userDetails)) {

                System.out.println("JWT VALID");
                System.out.println("USER = " + userDetails.getUsername());
                System.out.println("AUTHORITIES = " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {

            // Delete expired/invalid JWT cookie
            Cookie cookie = new Cookie("jwt", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // local development
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT expired or invalid.");

        }
    }
}
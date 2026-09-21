package com.controller;

import com.dto.LoginRequest;
import com.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest login){

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());
        authenticationManager.authenticate(token);

        return jwtService.generateToken(login.getEmail());

    }
}

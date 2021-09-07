package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.AuthRequest;
import com.mvpmatch.vendingmachine.dto.view.AuthView;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import com.mvpmatch.vendingmachine.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserAuth {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    public AuthView authUser(AuthRequest request) throws BadCredentialsException {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = (User) authenticate.getPrincipal();
        return new AuthView(jwtTokenUtil.generateAccessToken(user));
    }

    @Override
    public User getUser() {
        return userRepository.findByUsername(getAuthentication().getName());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}


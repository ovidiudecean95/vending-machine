package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.mapper.UserMapper;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service @Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuth userAuth;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public UserView create(CreateUserRequest createUserRequest) {
        User user = userMapper.createUserRequestToUser(createUserRequest);
        userRepository.save(user);
        return userMapper.userToUserView(user);
    }

    public void deleteAuthenticatedUser() {
        User user = userAuth.getUser();
        user.setDeleted(true);
        userRepository.save(user);
    }

    public UserView getAuthenticatedUser() {
        User user = userAuth.getUser();
        return userMapper.userToUserView(user);
    }

    public UserView updateAuthenticatedUser(UpdateUserRequest userRequest) {
        User user = userAuth.getUser();
        userMapper.update(userRequest, user);
        user = userRepository.save(user);
        return userMapper.userToUserView(user);
    }
}

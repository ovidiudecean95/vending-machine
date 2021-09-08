package com.mvpmatch.vendingmachine.controller;


import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Users")
@RestController
@RequestMapping(path = "api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/current")
    public UserView updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateAuthenticatedUser(request);
    }

    @DeleteMapping("/current")
    public ResponseEntity<Object> deleteCurrentUser() {
        userService.deleteAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/current")
    public UserView getCurrentUser() {
        return userService.getAuthenticatedUser();
    }

}

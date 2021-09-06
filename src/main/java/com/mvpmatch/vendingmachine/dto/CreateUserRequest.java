package com.mvpmatch.vendingmachine.dto;

import com.mvpmatch.vendingmachine.validator.Role;
import com.mvpmatch.vendingmachine.validator.UniqueUsername;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CreateUserRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 6, max = 20, message = "Username length should have 6-20 characters")
    @UniqueUsername
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 20, message = "Password length should have 8-20 characters")
    private String password;

    @NotBlank(message = "Role must not be blank")
    @Role
    private String role;

}

package com.mvpmatch.vendingmachine.dto;

import com.mvpmatch.vendingmachine.validator.Role;
import com.mvpmatch.vendingmachine.validator.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 6, max = 20, message = "Username length should have 6-20 characters")
    @UniqueUsername
    private String username;

}

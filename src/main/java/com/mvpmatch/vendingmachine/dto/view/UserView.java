package com.mvpmatch.vendingmachine.dto.view;

import lombok.Data;

@Data
public class UserView {

    private Integer id;

    private String username;

    private Integer deposit;

    private String role;

}

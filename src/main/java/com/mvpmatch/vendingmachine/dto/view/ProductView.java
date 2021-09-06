package com.mvpmatch.vendingmachine.dto.view;

import lombok.Data;

@Data
public class ProductView {

    private Integer id;
    private Integer amountAvailable;
    private Integer cost;
    private String productName;

}

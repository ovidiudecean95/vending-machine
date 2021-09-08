package com.mvpmatch.vendingmachine.dto.view;

import lombok.Data;

@Data
public class BoughtProductView {

    private Integer id;
    private Integer boughtAmount;
    private Integer cost;
    private String name;

}

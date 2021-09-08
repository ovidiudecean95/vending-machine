package com.mvpmatch.vendingmachine.dto.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BuyView {

    private Integer spent;
    private List<CoinView> change;
    private BoughtProductView products;

}

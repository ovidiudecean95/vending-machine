package com.mvpmatch.vendingmachine.dto.view;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinView {

    private Integer value;
    private Integer amount;

    public void incrementAmount() {
        ++amount;
    }

}

package com.mvpmatch.vendingmachine.dto;

import com.mvpmatch.vendingmachine.validator.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositCoinRequest {

    @Coin
    private Integer coin;

}

package com.mvpmatch.vendingmachine.dto.view;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinsView {

    private List<CoinView> coins;

}

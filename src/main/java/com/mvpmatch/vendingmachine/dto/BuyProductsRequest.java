package com.mvpmatch.vendingmachine.dto;

import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.validator.buy.BuyProducts;
import com.mvpmatch.vendingmachine.validator.EntityId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuyProducts
public class BuyProductsRequest {

    private Integer productId;

    @Min(1)
    private Integer amount;

}

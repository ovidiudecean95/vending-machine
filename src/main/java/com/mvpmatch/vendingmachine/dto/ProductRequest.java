package com.mvpmatch.vendingmachine.dto;

import com.mvpmatch.vendingmachine.validator.ProductPrice;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ProductRequest {

    @Min(0)
    @Max(20)
    private Integer amountAvailable;

    @ProductPrice
    private Integer cost;

    @NotBlank(message = "Product name must not be blank")
    private String productName;

}

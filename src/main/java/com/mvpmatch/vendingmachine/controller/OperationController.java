package com.mvpmatch.vendingmachine.controller;


import com.mvpmatch.vendingmachine.dto.BuyProductsRequest;
import com.mvpmatch.vendingmachine.dto.DepositCoinRequest;
import com.mvpmatch.vendingmachine.dto.view.BuyView;
import com.mvpmatch.vendingmachine.dto.view.CoinsView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.service.OperationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Operation")
@RestController
@Slf4j
@RequestMapping(path = "api")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/reset")
    public CoinsView resetDeposit() {
        return operationService.resetDeposit();
    }

    @PostMapping("/deposit")
    public UserView deposit(@RequestBody @Valid DepositCoinRequest depositCoinRequest) {
        return operationService.deposit(depositCoinRequest);
    }

    @PostMapping("/buy")
    public BuyView buy(@RequestBody @Valid BuyProductsRequest buyProductsRequest) {
        return operationService.buyProducts(buyProductsRequest);
    }

}

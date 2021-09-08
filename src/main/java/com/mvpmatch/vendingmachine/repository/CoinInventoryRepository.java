package com.mvpmatch.vendingmachine.repository;

import com.mvpmatch.vendingmachine.model.CoinInventory;
import com.mvpmatch.vendingmachine.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinInventoryRepository extends JpaRepository<CoinInventory, Integer> {

    CoinInventory findByCoinValue(Integer value);

}

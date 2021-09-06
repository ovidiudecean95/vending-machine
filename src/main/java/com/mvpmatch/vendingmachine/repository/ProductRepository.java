package com.mvpmatch.vendingmachine.repository;

import com.mvpmatch.vendingmachine.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}

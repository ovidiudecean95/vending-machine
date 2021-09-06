package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.mapper.ProductMapper;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductView getProduct(Integer id) {
        return productMapper.productToProductView(productRepository.getById(id));
    }
}

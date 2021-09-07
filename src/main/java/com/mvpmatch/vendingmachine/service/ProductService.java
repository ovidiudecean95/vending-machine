package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.mapper.ProductMapper;
import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserAuth userAuth;

    @Autowired
    private ProductMapper productMapper;

    public ProductView getProduct(Integer id) {
        return productMapper.productToProductView(productRepository.getById(id));
    }

    public List<ProductView> getProducts() {
        return productMapper.productsToProductViews(productRepository.findAll());
    }

    public ProductView create(ProductRequest productRequest) {
        Product product = productMapper.productRequestToProduct(productRequest);
        product.setSeller(userAuth.getUser());
        productRepository.save(product);
        return productMapper.productToProductView(product);
    }

    public void delete(Integer id) {
        Product product = productRepository.getById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }

    public ProductView update(ProductRequest productRequest, Integer id) {
        Product product = productRepository.getById(id);
        productMapper.update(productRequest, product);
        product = productRepository.save(product);
        return productMapper.productToProductView(product);
    }
}

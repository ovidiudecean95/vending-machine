package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.service.ProductService;
import com.mvpmatch.vendingmachine.validator.EntityId;
import com.mvpmatch.vendingmachine.validator.product.ProductEntityId;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Products")
@RestController
@RequestMapping(path = "api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("{id}")
    public ProductView getProduct(@PathVariable @EntityId(clazz = Product.class) Integer id) {
        return productService.getProduct(id);
    }

    @GetMapping()
    public List<ProductView> getProducts() {
        return productService.getProducts();
    }

    @PostMapping()
    public ProductView createProduct(@RequestBody @Valid ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("{id}")
    public ProductView update(@RequestBody @Valid ProductRequest request, @PathVariable @ProductEntityId Integer id) {
        return productService.update(request, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable @ProductEntityId Integer id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

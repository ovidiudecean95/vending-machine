package com.mvpmatch.vendingmachine.mapper;

import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    public abstract Product productRequestToProduct(ProductRequest productRequest);

    public abstract ProductView productToProductView(Product product);

    public abstract List<ProductView> productsToProductViews(List<Product> products);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void update(ProductRequest request, @MappingTarget Product user);

}

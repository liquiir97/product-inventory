package com.product.inventory.mapper;

import com.product.inventory.domain.Product;
import com.product.inventory.domain.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product product);

    Product toEntity(ProductDTO productDTO);
}

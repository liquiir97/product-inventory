package com.product.inventory.service;

import com.product.inventory.domain.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ProductService {

    ProductDTO save(ProductDTO productDTO);

    Optional<ProductDTO> findOne(Long id);

    Page<ProductDTO> findAll(Pageable pageable);

    ProductDTO update(ProductDTO productDTO);

    void delete(Long id);
}

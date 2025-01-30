package com.product.inventory.service;

import com.product.inventory.domain.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    CategoryDTO save(CategoryDTO categoryDTO);

    Optional<CategoryDTO> findOne(Long id);

    Page<CategoryDTO> findAll(Pageable pageable);

    CategoryDTO update(CategoryDTO categoryDTO);

    void delete(Long id);
}

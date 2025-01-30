package com.product.inventory.mapper;

import com.product.inventory.domain.Category;
import com.product.inventory.domain.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}

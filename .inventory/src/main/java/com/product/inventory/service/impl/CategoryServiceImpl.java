package com.product.inventory.service.impl;

import com.product.inventory.domain.Category;
import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.mapper.CategoryMapper;
import com.product.inventory.repository.CategoryRepository;
import com.product.inventory.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper)
    {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public CategoryDTO save(CategoryDTO categoryDTO)
    {
        LOG.debug("Request to save Category : {}", categoryDTO);
        Category category = this.categoryMapper.toEntity(categoryDTO);
        category = this.categoryRepository.save(category);
        return this.categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findOne(Long id)
    {
        LOG.debug("Request to get Category : {}", id);
        return this.categoryRepository.findById(id).map(ele->this.categoryMapper.toDto(ele));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable)
    {
        LOG.debug("Request to get all Categories");
        return this.categoryRepository.findAll(pageable).map(ele->this.categoryMapper.toDto(ele));
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO)
    {
        LOG.debug("Request to update Category : {}", categoryDTO);
        Category category = this.categoryMapper.toEntity(categoryDTO);
        category = this.categoryRepository.save(category);
        return this.categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long id)
    {
        LOG.debug("Request to delete Category : {}", id);
        this.categoryRepository.deleteById(id);
    }

}

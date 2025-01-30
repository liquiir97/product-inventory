package com.product.inventory.service.impl;

import com.product.inventory.domain.Product;
import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.domain.dto.ProductFilterDTO;
import com.product.inventory.mapper.CategoryMapper;
import com.product.inventory.mapper.ProductMapper;
import com.product.inventory.repository.ProductRepository;
import com.product.inventory.repository.specification.ProductSpecification;
import com.product.inventory.service.CategoryService;
import com.product.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryService categoryService, CategoryMapper categoryMapper)
    {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO)
    {
        LOG.debug("Request to save Product : {}", productDTO);
        if(productDTO.getCategory() != null) {
            Optional<CategoryDTO> categoryDTO = this.categoryService.findOne(productDTO.getCategory().getId());
            if (categoryDTO.isPresent()) {
                productDTO.setCategory(categoryDTO.get());
            }
        }
        Product product = this.productMapper.toEntity(productDTO);
        product = this.productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id)
    {
        LOG.debug("Request to get Product : {}", id);
        return this.productRepository.findById(id).map(ele->this.productMapper.toDto(ele));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable)
    {
        LOG.debug("Request to get all Products");
        return this.productRepository.findAll(pageable).map(ele->this.productMapper.toDto(ele));
    }

    @Override
    public ProductDTO update(ProductDTO productDTO)
    {
        LOG.debug("Request to update Product : {}", productDTO);
        if(productDTO.getCategory() != null)
        {
            Optional<CategoryDTO> categoryDTO = this.categoryService.findOne(productDTO.getCategory().getId());
            if(categoryDTO.isPresent())
            {
                productDTO.setCategory(categoryDTO.get());
            }
        }
        Optional<Product> product = this.productRepository.findById(productDTO.getId());
        Product productWithVersion = product.get();
        productWithVersion.setCategory(this.categoryMapper.toEntity(productDTO.getCategory()));
        productWithVersion.setId(productDTO.getId());
        productWithVersion.setName(productDTO.getName());
        productWithVersion.setDescription(productDTO.getDescription());
        productWithVersion.setPrice(productDTO.getPrice());
        productWithVersion.setQuantity(productDTO.getQuantity());
        productWithVersion = this.productRepository.save(productWithVersion);

        return this.productMapper.toDto(productWithVersion);
    }

    @Override
    public void delete(Long id)
    {
        LOG.debug("Request to delete Product : {}", id);
        this.productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllByFilter(ProductFilterDTO productFilterDTO, Pageable pageable)
    {
        LOG.debug("Request to get all Products using filter");
        return this.productRepository.findAll(ProductSpecification.filterByCriteria(productFilterDTO), pageable).map(ele->this.productMapper.toDto(ele));
    }
}

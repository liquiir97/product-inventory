package com.product.inventory.service.impl;

import com.product.inventory.domain.Product;
import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.mapper.ProductMapper;
import com.product.inventory.repository.ProductRepository;
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

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper)
    {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO)
    {
        LOG.debug("Request to save News : {}", productDTO);
        Product product = this.productMapper.toEntity(productDTO);
        product = this.productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id)
    {
        LOG.debug("Request to get News : {}", id);
        return this.productRepository.findById(id).map(ele->this.productMapper.toDto(ele));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable)
    {
        LOG.debug("Request to get all News");
        return this.productRepository.findAll(pageable).map(ele->this.productMapper.toDto(ele));
    }

    @Override
    public ProductDTO update(ProductDTO productDTO)
    {
        LOG.debug("Request to update Product : {}", productDTO);
        Product product = this.productMapper.toEntity(productDTO);
        product = this.productRepository.save(product);
        return this.productMapper.toDto(product);
    }

    @Override
    public void delete(Long id)
    {
        LOG.debug("Request to delete Product : {}", id);
        this.productRepository.deleteById(id);
    }
}

package com.product.inventory.controller;

import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.domain.dto.ProductFilterDTO;
import com.product.inventory.repository.CategoryRepository;
import com.product.inventory.repository.ProductRepository;
import com.product.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private static final String ENTITY_NAME = "product";

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, ProductRepository productRepository, CategoryRepository categoryRepository)
    {
        this.productService = productService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDTO)
    {
        LOG.debug("REST request to save Product : {}", productDTO);

        if(productDTO.getId() != null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New " + ENTITY_NAME + " cannot have an ID");
        }

        if(productDTO.getCategory() != null && productDTO.getCategory().getId() != null && !this.categoryRepository.existsById(productDTO.getCategory().getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no category with this id " + productDTO.getCategory().getId().toString());
        }

        productDTO = this.productService.save(productDTO);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@ParameterObject Pageable pageable)
    {
        LOG.debug("REST request to get a page of Products");
        Page<ProductDTO> productDTOPage = this.productService.findAll(pageable);
        return ResponseEntity.ok().body(productDTOPage);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id)
    {
        LOG.debug("GET request to get Product by id {}", id);

        Optional<ProductDTO> productDTOOptional = this.productService.findOne(id);
        return productDTOOptional.map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable(value = "id", required = false) final Long id, @RequestBody ProductDTO productDTO)
    {
        LOG.debug("REST request to update Product : {}, {}", id, productDTO);
        if(productDTO.getId() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id, id can not be null");
        }

        if (!Objects.equals(id, productDTO.getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        if (!this.productRepository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entity not found: id not found");
        }

        if(productDTO.getCategory() != null && productDTO.getCategory().getId() != null && !this.categoryRepository.existsById(productDTO.getCategory().getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no category with this id " + productDTO.getCategory().getId().toString());
        }

        productDTO = this.productService.update(productDTO);
        return ResponseEntity.ok().body(productDTO);
    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id)
    {
        LOG.debug("REST request to delete Product : {}", id);
        this.productService.delete(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/filter" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductDTO>> findAllByFilter(@ParameterObject ProductFilterDTO productFilterDTO, @ParameterObject Pageable pageable)
    {
        LOG.debug("REST request to get Product by filter");
        if(pageable == null)
        {
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            pageable = PageRequest.of(0,10,sort);
        }
        Page<ProductDTO> productDTOPage = this.productService.findAllByFilter(productFilterDTO, pageable);
        return ResponseEntity.ok().body(productDTOPage);
    }
}

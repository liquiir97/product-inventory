package com.product.inventory.controller;

import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.repository.ProductRepository;
import com.product.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.tomcat.util.http.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private static final String ENTITY_NAME = "product";

    private final ProductService productService;

    private final ProductRepository productRepository;

    public ProductController(ProductService productService, ProductRepository productRepository)
    {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDTO)
    {
        LOG.debug("REST request to save Product : {}", productDTO);

        if(productDTO.getId() != null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New " + ENTITY_NAME + " cannot have an ID");
        }

        productDTO = this.productService.save(productDTO);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductDTO>> getAllNews(Pageable pageable)
    {
        LOG.debug("REST request to get a page of Products");
        Page<ProductDTO> productDTOPage = this.productService.findAll(pageable);
        return ResponseEntity.ok().body(productDTOPage);
    }

    @Operation(summary = "Get all products", description = "Returns a paginated list of products")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id)
    {
        LOG.debug("GET request to get Product by id {}", id);

        Optional<ProductDTO> productDTOOptional = this.productService.findOne(id);
        return productDTOOptional.map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
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

        productDTO = this.productService.update(productDTO);
        return ResponseEntity.ok().body(productDTO);


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id)
    {
        LOG.debug("REST request to delete Product : {}", id);
        this.productService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}

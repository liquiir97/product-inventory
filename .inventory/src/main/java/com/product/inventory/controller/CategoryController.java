package com.product.inventory.controller;

import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.repository.CategoryRepository;
import com.product.inventory.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository)
    {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }


    @PostMapping("")
    public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        LOG.debug("REST request to save Category : {}", categoryDTO);

        if(categoryDTO.getId() != null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New category cannot have an ID");
        }

        categoryDTO = this.categoryService.save(categoryDTO);

        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(Pageable pageable)
    {
        LOG.debug("REST request to get a page of Categories");
        Page<CategoryDTO> categoryDTOPage = this.categoryService.findAll(pageable);
        return ResponseEntity.ok().body(categoryDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") Long id)
    {
        LOG.debug("GET request to get Category by id {}", id);

        Optional<CategoryDTO> categoryDTOOptional = this.categoryService.findOne(id);
        return categoryDTOOptional.map(categoryDTO -> new ResponseEntity<>(categoryDTO, HttpStatus.OK)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable(value = "id", required = false) final Long id, @RequestBody CategoryDTO categoryDTO)
    {
        LOG.debug("REST request to update Category : {}, {}", id, categoryDTO);
        if(categoryDTO.getId() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id, id can not be null");
        }

        if (!Objects.equals(id, categoryDTO.getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        if (!this.categoryRepository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entity not found: id not found");
        }

        categoryDTO = this.categoryService.update(categoryDTO);
        return ResponseEntity.ok().body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id)
    {
        LOG.debug("REST request to delete Product : {}", id);
        this.categoryService.delete(id);
        return  ResponseEntity.noContent().build();
    }

}

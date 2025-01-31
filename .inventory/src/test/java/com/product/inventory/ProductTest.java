package com.product.inventory;

import com.product.inventory.controller.ProductController;
import com.product.inventory.domain.Category;
import com.product.inventory.domain.Product;
import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.domain.dto.ProductDTO;
import com.product.inventory.mapper.ProductMapper;
import com.product.inventory.repository.CategoryRepository;
import com.product.inventory.repository.ProductRepository;
import com.product.inventory.service.CategoryService;
import com.product.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductController productController;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }


    @Test
    public void shouldSaveProduct() throws Exception {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Product 1");
        productDTO.setDescription("Product Description");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(10L);
        productDTO.setCategory(categoryDTO);

        Category category = new Category();
        category.setId(1L);
        category.setNameCategory("Electronics");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Product 1");
        savedProduct.setDescription("Product Description");
        savedProduct.setPrice(100.0);
        savedProduct.setQuantity(10L);
        savedProduct.setCategory(category);

        ProductDTO savedProductDto = new ProductDTO();
        savedProductDto.setId(1L);
        savedProductDto.setName("Product 1");
        savedProductDto.setDescription("Product Description");
        savedProductDto.setPrice(100.0);
        savedProductDto.setQuantity(10L);
        savedProductDto.setCategory(categoryDTO);

        when(productService.save(any(ProductDTO.class))).thenReturn(savedProductDto);
        when(categoryRepository.existsById(any(Long.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Product 1\",\"description\":\"Product Description\",\"price\":100.0,\"quantity\":10,\"category\":{\"id\":1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.description").value("Product Description"));
    }

    @Test
    public void shouldGetProductById() throws Exception {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setNameCategory("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product Description");
        product.setPrice(100.0);
        product.setQuantity(10L);
        product.setCategory(category);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Product 1");
        productDTO.setDescription("Product Description");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(10L);
        productDTO.setCategory(categoryDTO);

        when(productService.findOne(anyLong())).thenReturn(Optional.of(productDTO));

        // When & Then
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.description").value("Product Description"));
    }

    @Test
    public void shouldReturn404WhenProductNotFound() throws Exception {
        // Given
        when(productService.findOne(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllProducts() throws Exception {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");

        ProductDTO product1 = new ProductDTO();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Product Description 1");
        product1.setPrice(100.0);
        product1.setQuantity(10L);
        product1.setCategory(categoryDTO);

        ProductDTO product2 = new ProductDTO();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Product Description 2");
        product2.setPrice(200.0);
        product2.setQuantity(5L);
        product2.setCategory(categoryDTO);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<ProductDTO> productPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);
        when(productService.findAll(any(Pageable.class))).thenReturn(productPage);
        //when(productService.findAll(any(Pageable.class))).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/product")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))) // Check that we have 2 products in content
                .andExpect(jsonPath("$.content[0].name").value("Product 1"))
                .andExpect(jsonPath("$.content[1].name").value("Product 2"));
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");

        // ProductDTO to be updated
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L); // ID must be set to match the URL
        productDTO.setName("Updated Product");
        productDTO.setDescription("Updated Description");
        productDTO.setPrice(150.0);
        productDTO.setQuantity(20L);
        productDTO.setCategory(categoryDTO);

        // Updated ProductDTO response
        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setId(1L); // ID should be the same as the one in the request
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(150.0);
        updatedProduct.setQuantity(20L);
        updatedProduct.setCategory(categoryDTO);

        // Mock the productService's update method
        when(productService.update(any(ProductDTO.class))).thenReturn(updatedProduct);

        // Mock the categoryRepository.existsById check (this is part of the validation)
        when(categoryRepository.existsById(anyLong())).thenReturn(true); // Mocking that category with id exists

        // Mock categoryService.findOne to return categoryDTO when searching by ID
        //when(categoryService.findOne(anyLong())).thenReturn(Optional.of(categoryDTO));

        // Mock productRepository.existsById to ensure the product exists in the database
        when(productRepository.existsById(anyLong())).thenReturn(true); // Mocking that product with id exists

        // When & Then: Perform PUT request to update the product
        mockMvc.perform(put("/api/product/1") // Make sure the ID in URL is 1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"description\":\"Updated Description\",\"price\":150.0,\"quantity\":20,\"category\":{\"id\":1}}")) // Include the ID in the body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        // Given
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product 1");

        // When & Then
        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isNoContent());
    }


}

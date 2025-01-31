package com.product.inventory;

import com.product.inventory.controller.CategoryController;
import com.product.inventory.domain.dto.CategoryDTO;
import com.product.inventory.repository.CategoryRepository;
import com.product.inventory.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void shouldSaveCategory() throws Exception {

        CategoryDTO categoryDTOSaved = new CategoryDTO();
        categoryDTOSaved.setId(1L);
        categoryDTOSaved.setNameCategory("Electronics");

        when(categoryService.save(any(CategoryDTO.class))).thenReturn(categoryDTOSaved);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nameCategory\":\"Electronics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nameCategory").value("Electronics"));

    }

    @Test
    public void shouldGetCategoryById() throws Exception {


        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");


        when(categoryService.findOne(anyLong())).thenReturn(Optional.of(categoryDTO));

        // When & Then
        mockMvc.perform(get("/api/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nameCategory").value("Electronics"));
    }

    @Test
    public void shouldReturn404WhenCategoryNotFound() throws Exception {

        when(categoryService.findOne(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/category/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllCategories() throws Exception {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2L);
        categoryDTO2.setNameCategory("Sport");


        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<CategoryDTO> categoryDTOPage = new PageImpl<>(Arrays.asList(categoryDTO, categoryDTO2), pageable, 2);
        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryDTOPage);


        // When & Then
        mockMvc.perform(get("/api/category")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))) // Check that we have 2 products in content
                .andExpect(jsonPath("$.content[0].nameCategory").value("Electronics"))
                .andExpect(jsonPath("$.content[1].nameCategory").value("Sport"));
    }

    @Test
    public void shouldUpdateCategory() throws Exception {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Electronics");


        CategoryDTO categoryDTOUpdated = new CategoryDTO();
        categoryDTOUpdated.setId(1L);
        categoryDTOUpdated.setNameCategory("Sport");

        when(categoryService.update(any(CategoryDTO.class))).thenReturn(categoryDTOUpdated);

        when(categoryRepository.existsById(anyLong())).thenReturn(true);

        when(categoryRepository.existsById(anyLong())).thenReturn(true); // Mocking that product with id exists

        // When & Then: Perform PUT request to update the category
        mockMvc.perform(put("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Sport\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameCategory").value("Sport"));
    }

    @Test
    public void shouldDeleteCategory() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setNameCategory("Sport");

        // When & Then
        mockMvc.perform(delete("/api/category/1"))
                .andExpect(status().isNoContent());
    }
}

package com.product.inventory.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.product.inventory.domain.dto.validation.StrictDoubleDeserializer;
import com.product.inventory.domain.dto.validation.StrictLongDeserializer;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {

    private Long id;
    @NotNull(message = "Product name is required.")
    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;
    @NotNull(message = "Product description is required.")
    @NotBlank(message = "Product description cannot be blank")
    @Size(min = 1, max = 500, message = "Product description must be between 5 and 500 characters.")
    private String description;
    @NotNull(message = "Product price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @JsonDeserialize(using = StrictDoubleDeserializer.class)
    private Double price;
    @NotNull(message = "Product quantity is required.")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @JsonDeserialize(using = StrictLongDeserializer.class)
    private Long quantity;

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", price='" + getPrice() + "'" +
                ", quantity='" + getQuantity() + "'" +
                ", category='" + getCategory() + "'" +
                "}";
    }
}

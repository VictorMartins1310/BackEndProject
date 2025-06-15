package com.bootcamp.project.dto;

import com.bootcamp.project.model.ProductType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDTO {
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String name;
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String brand;
    private BigDecimal price;
    private int qty;
    private ProductType type;
    private Boolean bought = false;
}
package com.bootcamp.project.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShoppingListProductsDTO extends ToDoListDTO{
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String marketName;
    private List<ProductDTO> products = new ArrayList<>();
}
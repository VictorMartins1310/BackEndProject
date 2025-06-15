package com.bootcamp.project.controller;

import com.bootcamp.project.dto.ProductDTO;

public interface ProductController {
    Object addProduct(Long todoID, ProductDTO product);
    Object boughtProduct(Long idOfProduct, int qty);
    Object updateProduct(Long idOfProduct, String name, String brand, String price);
}
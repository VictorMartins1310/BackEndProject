package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.ProductDTO;

public interface ProductController {
    Object addProduct(Long todoID, ProductDTO product);
    Object boughtProduct(Long idOfProduct, int qty);
    Object updateProduct(Long idOfProduct, String name, String brand, String price);
}
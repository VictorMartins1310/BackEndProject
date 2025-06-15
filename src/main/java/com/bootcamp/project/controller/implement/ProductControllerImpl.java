package com.bootcamp.project.controller.implement;

import com.bootcamp.project.controller.ProductController;
import com.bootcamp.project.dto.ProductDTO;
import com.bootcamp.project.dto.ShoppingListProductsDTO;
import com.bootcamp.project.mappers.ProductMapper;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.Product;
import com.bootcamp.project.service.ProductService;
import com.bootcamp.project.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** This Controller is destined for Products
 * It can Create and Update a Product
 * A delete function here is not needed,it can only be deleted by deleting the Shopping List
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(name = "products", value = "todolist/shoppinglist/{idOfShoppingList}")
public class ProductControllerImpl implements ProductController {
    private final ShoppingListService shoppingService;
    private final ProductService productService;

    private final TodoListMapper shoppingLMapper;
    private final ProductMapper productMapper;
    @PostMapping(value = "/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingListProductsDTO addProduct(@PathVariable("idOfShoppingList") Long todoID, @RequestBody ProductDTO productDTO){
        Product product = productMapper.toEntity(productDTO);
        return shoppingLMapper.toDTO(shoppingService.addProduct2List(todoID, product));
    }

    // A get Product dont make sense here, it makes sense get All Products but make sense Update a Product

    @Override
    @PatchMapping(value = "/products/{idOfProduct}/bought")
    @ResponseStatus(HttpStatus.OK)
    public Boolean boughtProduct(@PathVariable("idOfProduct") Long idOfProduct, @RequestParam("qty") int qty) {
        productService.productBought(idOfProduct, qty);
        return true;
    }
    @PatchMapping(value = "/product/{idOfProduct}")
    @ResponseStatus(HttpStatus.OK)
    public Product updateProduct(
            @PathVariable("idOfProduct") Long idOfProduct,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "price", required = false) String price
            ) {

        return productService.updateProduct(idOfProduct, name, brand, price);
    }
}
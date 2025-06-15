package com.bootcamp.project.controllerTest;

import com.bootcamp.project.controller.implement.ProductControllerImpl;
import com.bootcamp.project.dto.ProductDTO;
import com.bootcamp.project.dto.ShoppingListProductsDTO;
import com.bootcamp.project.mappers.ProductMapper;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.Product;
import com.bootcamp.project.model.ProductType;
import com.bootcamp.project.service.ProductService;
import com.bootcamp.project.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ProductControllerImpl.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TodoListMapper shoppingLMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private ShoppingListService shoppingLService;
    @MockBean
    private ProductMapper productMapper;

    private final Long productID = 13L;
    private final Product product =
            new Product("Coca Cola", "Coca Cola Company", new BigDecimal("1.23"), 4, ProductType.Drink);

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("Test: Create a Product")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testCreateProduct() throws Exception {
        Long ShoppingListID = 13L;

        ShoppingListProductsDTO shoppingListProducts = new ShoppingListProductsDTO();
        ShoppingListProductsDTO shoppingListProducts2 = new ShoppingListProductsDTO();

        ProductDTO productDto1 = new ProductDTO();
        ProductDTO productDto2 = new ProductDTO();
        productDto1.setName(product.getName());
        productDto2.setName("Pepsi");

        List<ProductDTO> productDTOList1 = new ArrayList<>();
        productDTOList1.add(productDto1);

        List<ProductDTO> productDTOList2 = new ArrayList<>();

        productDTOList2.add(productDto2);

        shoppingListProducts.setProducts(productDTOList1);
        shoppingListProducts2.setProducts(productDTOList2);

        when(shoppingLMapper.toDTO(shoppingLService.addProduct2List(ShoppingListID, product))).thenReturn(shoppingListProducts);
        mockMvc.perform(post("/todolist/shoppinglist/{idOfShoppingList}/products", productID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListProducts))); // Change to 2 for fail
    }
    @DisplayName("Test: Update a Product")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testUpdateProduct() throws Exception {
        String  newProductName = "Pepsi";
        
        product.setProductID(productID);

        Product product2 = new Product(newProductName, product.getBrand(), product.getPrice(), product.getQty(), product.getType());
        product2.setProductID(productID);


        when(productService.updateProduct(productID, newProductName, null, null)).thenReturn(product2);

        mockMvc.perform(
                patch("/todolist/shoppinglist/{idOfShoppingList}/product/{id}", productID.toString(), productID.toString())
                        .queryParam("name", newProductName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product2))); //change to get Fail
    }
}

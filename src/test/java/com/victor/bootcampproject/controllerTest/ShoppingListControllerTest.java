package com.victor.bootcampproject.controllerTest;

import com.victor.bootcampproject.controller.implement.ShoppingListControllerImpl;
import com.victor.bootcampproject.dto.ProductDTO;
import com.victor.bootcampproject.dto.ShoppingListDTO;
import com.victor.bootcampproject.dto.ShoppingListProductsDTO;
import com.victor.bootcampproject.mappers.TodoListMapper;
import com.victor.bootcampproject.model.ShoppingList;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.ShoppingListService;
import com.victor.bootcampproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ShoppingListControllerImpl.class)
public class ShoppingListControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockitoBean
    private ShoppingListService shoppingListService;
    @MockitoBean private UserService userService;
    @MockitoBean private TodoListMapper shoppingLMapper;

    private final AppUser user = new AppUser("email@mail.com", "DumpPass1234");
    private final UUID userID = UUID.randomUUID();
    private final String
            marketNameIN = "ALDI",
            marketNameOUT = "LIDL";
    private final ShoppingList shoppingList = new ShoppingList(user, marketNameIN);
    private final Long shoppingListID = 13L;
    private final ShoppingListDTO shoppingListDTO1 = new ShoppingListDTO();
    private final ShoppingListDTO shoppingListDTO2 = new ShoppingListDTO();

    @BeforeEach
    public void setUp() {
        shoppingList.setTodoID(shoppingListID);

        shoppingListDTO1.setMarketName(marketNameIN);
        shoppingListDTO2.setMarketName(marketNameOUT);

        user.setUserID(userID);
    }

    @DisplayName("Test: Create Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testCreateShoppingList() throws Exception {

        when(shoppingLMapper.toDto(shoppingListService.newShoppingList(user, marketNameIN))).thenReturn(shoppingListDTO1);

        mockMvc.perform(post("/api/todolist/shoppinglist")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListDTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListDTO1))); // change to shoppingListDTO2 for Fail Test
    }

    @DisplayName("Test: Get Shoppinglists + Products")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testGetShoppingList() throws Exception {
        ProductDTO productDto1 = new ProductDTO();
        productDto1.setName("Cola");
        ShoppingListProductsDTO shoppingListDto = new ShoppingListProductsDTO();
        shoppingListDto.getProducts().add(productDto1);

        ProductDTO productDto2 = new ProductDTO();
        productDto2.setName("Pepsi");
        ShoppingListProductsDTO shoppingListDtoFail = new ShoppingListProductsDTO();
        shoppingListDtoFail.getProducts().add(productDto2);

        when(shoppingLMapper.toDTO(shoppingListService.getShoppingList(shoppingListID))).thenReturn(shoppingListDto);

        mockMvc.perform(
                get("/api/todolist/shoppinglist/{shopID}", shoppingListID.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListDto))); // Set shoppingListDtoFail to Fail Test
    }

    @DisplayName("Test: Update Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testUpdateShoppingList() throws Exception {
        ShoppingList shoppingListB = new ShoppingList(user, marketNameOUT);
        shoppingListB.setTodoID(shoppingListID);

        System.out.println(shoppingList);
        System.out.println(shoppingListB);

        when(shoppingListService.updateShoppingList(shoppingListID, marketNameOUT)).thenReturn(shoppingListB);

        mockMvc.perform(
                        patch("/api/todolist/shoppinglist/{shoppingLID}", shoppingList.getTodoID())
                                .with(csrf())
                                .queryParam("marketName", marketNameOUT))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListB))); // Set ShoppingListB to Fail Test
    }

    @DisplayName("Test: Delete Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testDeleteShoppingList() throws Exception {
        doNothing().when(shoppingListService).deleteShoppingList(shoppingListID);
        mockMvc.perform(
                        delete("/api/todolist/shoppinglist/{id}", shoppingListID.toString())
                                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
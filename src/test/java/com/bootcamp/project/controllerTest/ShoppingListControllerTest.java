package com.bootcamp.project.controllerTest;

import com.bootcamp.project.controller.implement.ShoppingListControllerImpl;
import com.bootcamp.project.dto.ProductDTO;
import com.bootcamp.project.dto.ShoppingListDTO;
import com.bootcamp.project.dto.ShoppingListProductsDTO;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.ShoppingList;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.ShoppingListService;
import com.bootcamp.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ShoppingListControllerImpl.class)
public class ShoppingListControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockBean private ShoppingListService shoppingListService;
    @MockBean private UserService userService;
    @MockBean private TodoListMapper shoppingLMapper;

    private final User user = new User("email@mail.com", "DumpPass1234");
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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        shoppingList.setTodoListID(shoppingListID);

        shoppingListDTO1.setMarketName(marketNameIN);
        shoppingListDTO2.setMarketName(marketNameOUT);
        shoppingListDTO1.setTodoListName(shoppingList.getTodoListName());
        shoppingListDTO2.setTodoListName(shoppingList.getTodoListName());

        user.setUserID(userID);
    }

    @DisplayName("Test: Create Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testCreateShoppingList() throws Exception {

        when(shoppingLMapper.toDto(shoppingListService.newShoppingList(user, marketNameIN))).thenReturn(shoppingListDTO1);

        mockMvc.perform(post("/todolist/shoppinglist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListDTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListDTO1))); // change to shoppingListDTO2 for Fail Test
    }
    @DisplayName("Test: Get All Shoppinglists")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testGetAllShoppingLists() throws Exception {
        List<ShoppingListDTO> shoppingListDto = new ArrayList<>();
        shoppingListDto.add(shoppingListDTO1);

        List<ShoppingListDTO> shoppingListDtoFail = new ArrayList<>();
        shoppingListDtoFail.add(shoppingListDTO2);

        when(shoppingLMapper.toShoppingListDtos(shoppingListService.getShoppingLists(user))).thenReturn(shoppingListDto);

        mockMvc.perform(get("/todolist/shoppinglist"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListDto))); // Change to shoppingListDtoFail for Fail Test
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

        mockMvc.perform(get("/todolist/shoppinglist/{shopID}", shoppingListID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingListDto))); // Set shoppingListDtoFail to Fail Test
    }

    @DisplayName("Test: Update Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    public void testUpdateShoppingList() throws Exception {
        ShoppingList shoppingListB = new ShoppingList(user, marketNameIN);
        shoppingListB.setTodoListID(shoppingListID);
        shoppingListB.setTodoListName(shoppingList.getTodoListName());
        shoppingListB.setMarketName(marketNameOUT);

        when(shoppingListService.updateShoppingList(shoppingListID, null, marketNameIN)).thenReturn(shoppingList);

        mockMvc.perform(
                        patch("/todolist/shoppinglist/{id}", shoppingListID.toString())
                                .queryParam("marketname", marketNameIN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shoppingList))); // Set ShoppingListB to Fail Test
    }

    @DisplayName("Test: Delete Shopping List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testDeleteShoppingList() throws Exception {
        doNothing().when(shoppingListService).deleteShoppingList(shoppingListID);
        mockMvc.perform(
                        delete("/todolist/shoppinglist/{id}", shoppingListID.toString()))
                .andExpect(status().isNoContent());
    }
}
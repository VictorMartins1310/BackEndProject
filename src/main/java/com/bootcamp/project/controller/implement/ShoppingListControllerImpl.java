package com.bootcamp.project.controller.implement;

import com.bootcamp.project.controller.ShoppingListController;
import com.bootcamp.project.dto.ShoppingListDTO;
import com.bootcamp.project.dto.ShoppingListProductsDTO;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.ShoppingList;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.ShoppingListService;
import com.bootcamp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** This Controller is destined for the Shopping List
* It can Create, Update and Delete a Shopping List
*/
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "shoppinglist", value = "api/todolist/shoppinglist")
public class ShoppingListControllerImpl implements ShoppingListController{
    // Services
    private final ShoppingListService shoppingLService;
    private final UserService userService;

    // Mappers
    private final TodoListMapper shoppingLMapper;

    // private fields
    private User loggedUser;
    private ShoppingList shoppingList;

    /**
     * This function get the Authenticated User
     * @return Authenticated User
     */
    private User getAuthUser() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingListDTO newShoppingList(@RequestBody ShoppingListDTO newShoppingData){
        loggedUser = getAuthUser();
        shoppingList = shoppingLService.newShoppingList(loggedUser, newShoppingData.getMarketName());
        return shoppingLMapper.toDto(shoppingList);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShoppingListDTO> showShoppingLists(){
        loggedUser = getAuthUser();
        return shoppingLMapper.toShoppingListDtos(shoppingLService.getShoppingLists(loggedUser));
    }
    @GetMapping(value = "/{shoppingLID}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListProductsDTO showShoppingList(@PathVariable("shoppingLID") Long id){
        shoppingList = shoppingLService.getShoppingList(id);
        return shoppingLMapper.toDTO(shoppingList);
    }
    @PatchMapping(value = "/{shoppingLID}")
    public ShoppingList updateShoppingList(@PathVariable("shoppingLID") Long id, @RequestParam(value = "todoListName", required = false) String todoListName , @RequestParam("marketName") String marketName){
        shoppingList = shoppingLService.updateShoppingList(id, todoListName, marketName);
        return shoppingList;
    }
    @DeleteMapping(value = "/{shoppingLID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShoppingList(@PathVariable("shoppingLID") Long id){
        shoppingLService.deleteShoppingList(id);
    }
}

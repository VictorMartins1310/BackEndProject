package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.ShoppingListController;
import com.victor.bootcampproject.dto.ShoppingListDTO;
import com.victor.bootcampproject.dto.ShoppingListProductsDTO;
import com.victor.bootcampproject.mappers.TodoListMapper;
import com.victor.bootcampproject.model.ShoppingList;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.ShoppingListService;
import com.victor.bootcampproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private AppUser loggedUser;
    private ShoppingList shoppingList;

    /**
     * This function get the Authenticated User
     * @return Authenticated User
     */
    private AppUser getAuthUser() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingListDTO newShoppingList(@RequestBody ShoppingListDTO newShoppingData){
        loggedUser = getAuthUser();
        shoppingList = shoppingLService.newShoppingList(loggedUser, newShoppingData.getMarketName());
        return shoppingLMapper.toDto(shoppingList);
    }

    /**
     * Function to get All ShoppingList's by Authenticated User
     * @return A list of ShoppingList
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShoppingListDTO> showShoppingLists(){
        // TODO Re Develop no user in shoppinglist
        loggedUser = getAuthUser();
        List<ShoppingList> emptyList = new ArrayList<>();
        return shoppingLMapper.toDTO(emptyList);
        //return shoppingLMapper.toShoppingListDtos(shoppingLService.getShoppingLists(loggedUser));
    }
    /* Move to ProductsController
    @GetMapping(value = "/{shoppingLID}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListProductsDTO showShoppingList(@PathVariable("shoppingLID") Long id){
        shoppingList = shoppingLService.getShoppingList(id);
        return shoppingLMapper.toDTO(shoppingList);
    }
    */

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

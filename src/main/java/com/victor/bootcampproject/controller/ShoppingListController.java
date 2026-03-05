package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.ShoppingListDTO;
import com.victor.bootcampproject.model.AppUser;

public interface ShoppingListController {
    Object newShoppingList(AppUser optionalUser, ShoppingListDTO shoppingList);
    Object showShoppingList(Long id);
    Object updateShoppingList(Long id,String todolistname, String marketName);
    Object showShoppingLists(AppUser optionalUser);
    void deleteShoppingList(Long id);
}
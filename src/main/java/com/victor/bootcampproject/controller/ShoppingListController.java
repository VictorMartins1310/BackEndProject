package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.ShoppingListDTO;

public interface ShoppingListController {
    Object newShoppingList(ShoppingListDTO shoppingList);
    Object showShoppingList(Long id);
    Object updateShoppingList(Long id,String todolistname, String marketName);
    Object showShoppingLists();
    void deleteShoppingList(Long id);
}
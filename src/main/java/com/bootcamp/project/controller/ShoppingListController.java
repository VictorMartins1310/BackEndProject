package com.bootcamp.project.controller;

import com.bootcamp.project.dto.ShoppingListDTO;

public interface ShoppingListController {
    Object newShoppingList(ShoppingListDTO shoppingList);
    Object showShoppingList(Long id);
    Object updateShoppingList(Long id,String todolistname, String marketName);
    Object showShoppingLists();
    void deleteShoppingList(Long id);
}
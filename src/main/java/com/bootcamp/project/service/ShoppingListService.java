package com.bootcamp.project.service;

import com.bootcamp.project.exception.ProjectException;
import com.bootcamp.project.model.*;
import com.bootcamp.project.repos.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingListService {
    // Repositories
    private final ShoppingListRepository shoppingListRepository;
    // Services
    private final ProductService prodService;

    public Integer countShoppingListsByUser(User user){
        // Function maybe desnecessary
        return 1;
        //return shoppingListRepository.countShoppingListsByUser(user);
    }

    public ShoppingList getShoppingList(Long id) {
        if (shoppingListRepository.getShoppingListByTodoID(id).isEmpty())
            throw new ProjectException("Shopping List " + id + " Not Found");
        return shoppingListRepository.getShoppingListByTodoID(id).get();
    }

    // No longer in use
    /*public List<ShoppingList> getShoppingLists(User user) {
        if (shoppingListRepository.findShoppingListsByUser(user).isEmpty())
            throw new ProjectException("Shopping List Not Found");
        return shoppingListRepository.findShoppingListsByUser(user);
    }*/

    public ShoppingList newShoppingList(User user, String marketName) {
        ShoppingList shoppingList = new ShoppingList(user, marketName);
        return save(shoppingList);
    }

    public ShoppingList updateShoppingList(Long id, String toDoListName, String marketName) {
        //if (shoppingListRepository.findShoppingListByTodoListID(id).isEmpty())
            //throw new ProjectException("Shopping List " + id + " Not Found");
        //ShoppingList shoppingList = shoppingListRepository.findShoppingListByTodoListID(id).get();
        ShoppingList shoppingList = shoppingListRepository.findById(id).get();
        shoppingList.setMarketName(marketName);
        return save(shoppingList);
    }

    public ShoppingList addProduct2List(long shopID, Product prod) {
        ShoppingList shoppingList = getShoppingList(shopID);
        if (prod.getProductID() == null){
            Product savedProd = prodService.newProduct(prod);
            shoppingList.addProduct(savedProd);
        } else {
            // This was created for DataLoader, but gives me a Lazy Exception, so it works but can be Deleted
            shoppingList.addProduct(prod);
        }
        return save(shoppingList);
    }

    public ShoppingList save(ShoppingList shop){
        return shoppingListRepository.save(shop);
    }

    // TODO re-Develop function
    public void deleteShoppingLists(User user) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findShoppingListsByUser(user);
        if (countShoppingListsByUser(user) > 0)
            for (ShoppingList shoppingList : shoppingLists) {
                if (!shoppingList.getProducts().isEmpty())
                    prodService.deleteProducts(shoppingList.getProducts());
                // TODO correct next line, shoppilng list is not heratige of TODO LIST anymore
                //deleteShoppingList(shoppingList.getTodoListID());
            }
    }

    public void deleteShoppingList(Long productID) {
/*        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(productID);
        if (shoppingList.isPresent())
            shoppingListRepository.delete(shoppingList.get());
*/    }
}
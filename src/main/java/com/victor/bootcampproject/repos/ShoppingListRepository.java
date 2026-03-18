package com.victor.bootcampproject.repos;

import com.victor.bootcampproject.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    Optional<ShoppingList> getShoppingListByTodoID(Long id);
    List<ShoppingList> findShoppingListsByUser(AppUser user);
    void deleteShoppingListsByUser(AppUser user);
}
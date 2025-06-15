package com.bootcamp.project;

import com.bootcamp.project.model.*;
import com.bootcamp.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {
    private final UserService userService;
    private final ShoppingListService shoppingListService;

    /** This Dataloader fill Data if the Database is empty (by the logic there is no Users
     * Independently if application use create-drop or update  */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        long qtyUsers = userService.qtyUsers();
        if (qtyUsers == 0) {
            userService.addRole("ROLE_ADMIN");
            userService.addRole("ROLE_USER");

            User[] users = {
                    userService.newAdmin("Admin@mail.de", "badPassword"),
                    userService.newUser("User@mail.de", "badPassword")
            };
            shoppingListService.newShoppingList(users[1], "ALDI");
            shoppingListService.newShoppingList(users[1], "Penny");
            shoppingListService.newShoppingList(users[1], "NETTO");
        }
    }
}
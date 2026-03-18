package com.victor.bootcampproject.service;

import com.victor.bootcampproject.exception.ProjectException;
import com.victor.bootcampproject.model.*;
import com.victor.bootcampproject.repos.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService{
    protected final UserRepository userRepo;
    protected final RoleRepository roleRepository;

    // Service Section
    protected final ShoppingListService shoppingListService;

    /**  Injects a bean of type PasswordEncoder into this class.
     * The bean is used for encoding passwords before storing them.
     */
    protected final PasswordEncoder passwordEncoder;
    // Method Section
    public long qtyUsers(){ return userRepo.count(); }

    /** Show all Users a funtion for an Admin
     * @return List of Users without Password
     */
    public List<AppUser> showUsers(){  return userRepo.findAll(); }
    /** This Function add new Role or get the Role by Name
     * @param name String
     * @return new Role
     */
    public Role addRole(String name){
        if (roleRepository.findByRole(name).isEmpty()) {
            return roleRepository.save(new Role(name));
        }else
            return roleRepository.findByRole(name).get();
    }

    public AppUser save(@NonNull AppUser user, String role){
        user.addRole(addRole(role));
        return userRepo.save(user);
    }




    /** Creates a new User
     * In case don't exist a User it will create a User with ADMIN ROLE
     * Otherwise it creates a User with a Default TaskList and USER ROLE

     * @param email String
     * @param password String
     * @return User User
     */

    public AppUser newAdmin(UUID uuid, String email){
        AppUser user = new AppUser(uuid, email);
        return save(user, "ROLE_ADMIN");
    }

    public AppUser newUser(UUID uuid, String email){
        AppUser user = new AppUser(uuid, email);
        return save(user, "ROLE_USER");
    }

    public AppUser getUserByUserID(UUID id){
        if (userRepo.getUserByUserID(id).isEmpty())
            throw new ProjectException("User Not Found");
        return userRepo.getUserByUserID(id).get();
    }

    public AppUser getUserByEmail(String email){
        if (userRepo.getUserByEmail(email).isEmpty())
            throw new ProjectException("User Not Found");
        return userRepo.getUserByEmail(email).get();
    }
    public AppUser updateDetails(@NonNull AppUser loggedUser, String firstName, String lastName, String birthDate) {
        if (userRepo.getUserByUserID(loggedUser.getUserID()).isEmpty())
            throw new ProjectException("User Not Found");
        loggedUser.updateDetails(firstName, lastName, LocalDate.parse(birthDate));
        return userRepo.save(loggedUser);
    }

    public AppUser syncUserFromSupabase(UUID uuid, String email) {
        Optional<AppUser> user = userRepo.getUserByUserID(uuid);
        if (user.isPresent())
            return user.get();
        AppUser newUser = newUser(uuid, email);
        return userRepo.save(newUser);
    }

    public void deleteUserByID(UUID userID){
        AppUser user = getUserByUserID(userID);
        if (user == null)
            throw new ProjectException("User not Found");
        // Todo delete TodoItems
        //taskListService.deleteTasksLists(user);
        shoppingListService.deleteShoppingLists(user);
        userRepo.delete(user);
    }
}
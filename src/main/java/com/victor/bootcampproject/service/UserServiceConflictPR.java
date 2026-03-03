package com.victor.bootcampproject.service;

import com.victor.bootcampproject.exception.ProjectException;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Role;
import com.victor.bootcampproject.repos.RoleRepository;
import com.victor.bootcampproject.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public abstract class UserServiceConflictPR {
    protected final UserRepository userRepo;
    protected final RoleRepository roleRepository;

    // Service Section
    protected final ShoppingListService shoppingListService;

    protected final PasswordEncoder passwordEncoder;


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

    public AppUser newAdmin(String email, String password){
        AppUser user = new AppUser(email, password);
        return save(user, "ROLE_ADMIN");
    }

    public AppUser save(AppUser user, String role){
        user.addRole(addRole(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    /** Creates a new User
     * In case don't exist a User it will create a User with ADMIN ROLE
     * Otherwise it creates a User with a Default TaskList and USER ROLE

     * @param email String
     * @param password String
     * @return User User
     */
    public AppUser newUser(String email, String password){
        AppUser user = new AppUser(email, password);
        user.setUserID(UUID.randomUUID());
        return save(user, "ROLE_USER");
    }
    public AppUser newUser(UUID uuid, String email, String password){
        AppUser user = new AppUser(email, password);
        user.setUserID(uuid);
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
    public AppUser updateDetails(AppUser loggedUser, String firstName, String lastName, String birthDate) {
        if (userRepo.getUserByUserID(loggedUser.getUserID()).isEmpty())
            throw new ProjectException("User Not Found");
        loggedUser.updateDetails(firstName, lastName, LocalDate.parse(birthDate));
        return userRepo.save(loggedUser);
    }
    /** Loads the user by its username, in this case the email adress
     *
     * @param email the username to search for
     * @return the UserDetails object that matches the given username
     * @throws UsernameNotFoundException if the user with the given username is not found
     */

    public void deleteUserByID(UUID userID){
        AppUser user = getUserByUserID(userID);
        if (user == null)
            throw new ProjectException("User not Found");
        //taskListService.deleteTasksLists(user);
        shoppingListService.deleteShoppingLists(user);
        userRepo.delete(user);
    }

}
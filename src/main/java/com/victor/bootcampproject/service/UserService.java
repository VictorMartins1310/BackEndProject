package com.victor.bootcampproject.service;

import com.victor.bootcampproject.exception.ProjectException;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Role;
import com.victor.bootcampproject.repos.RoleRepository;
import com.victor.bootcampproject.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    // Repositories Section
    private final UserRepository userRepo;
    private final RoleRepository roleRepository;

    // Service Section
    private final ShoppingListService shoppingListService;

    /**  Injects a bean of type PasswordEncoder into this class.
     * The bean is used for encoding passwords before storing them.
     */
    private final PasswordEncoder passwordEncoder;
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
    // UserDetails Section
    public AppUser findByUserID(UUID id){
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
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user with the given username
        AppUser user = getUserByEmail(email);
        // Check if user exists
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            // Create a collection of SimpleGrantedAuthority objects from the user's roles
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            // Return the user details, including the username, password, and authorities
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
    }

    public void deleteUserByID(UUID userID){
        AppUser user = findByUserID(userID);
        if (user == null)
            throw new ProjectException("User not Found");
        //taskListService.deleteTasksLists(user);
        shoppingListService.deleteShoppingLists(user);
        userRepo.delete(user);
    }
}
package com.bootcamp.project.controllerTest;

import com.bootcamp.project.controller.implement.UserControllerImpl;
import com.bootcamp.project.dto.LoginDTO;
import com.bootcamp.project.dto.UserDetailsDTO;
import com.bootcamp.project.mappers.UserDetailsMapper;
import com.bootcamp.project.model.Role;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@WebMvcTest(UserControllerImpl.class)
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockBean private UserService userService;
    @MockBean private UserDetailsMapper userDetailsMapper;

    private final LocalDate birthdate = LocalDate.parse("1987-10-13");
    private final String
            firstName = "Victor",
            lastName = "Martins";

    private final User user1 = new User("email@mail.com", "Test.1234");
    private final User user2 = new User("other@mail.com", "Pass.1234");
    private final Role role = new Role();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        role.setRole("USER_ROLE");

        user1.setUserID(UUID.randomUUID());

        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setBirthDate(birthdate);
        user1.addRole(role);
        user2.setFirstName(firstName);
        user2.setLastName(lastName);
        user2.setBirthDate(birthdate);
        user2.addRole(role);
    }

    @DisplayName("Test: Adding new Users")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testCreateUser() throws Exception {
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail(user1.getEmail()); loginDto.setPassword(user1.getPassword());

        UserDetailsDTO userDto1 = new UserDetailsDTO(); UserDetailsDTO userDto2 = new UserDetailsDTO();

        userDto1.setEmail(user1.getEmail()); userDto2.setEmail(user2.getEmail());

        when(userDetailsMapper.toDto(userService.newUser("email@mail.com", "PassWORTd"))).thenReturn(userDto1);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto1)));
    }

    @DisplayName("Test: Get UsersDetails")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void getUserDetails() throws Exception {
        UserDetailsDTO userDetails = new UserDetailsDTO();
        userDetails.setEmail(user1.getEmail());
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setBirthDate(birthdate);

        when(userDetailsMapper.toDto(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))).thenReturn(userDetails);

        mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDetails))); //https://github.com/json-path/JsonPath
    }

    @DisplayName("Test: Update UsersDetails on Register")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testUpdateUser() throws Exception{
        UserDetailsDTO userDetailsDTO1 = new UserDetailsDTO(); UserDetailsDTO userDetailsDTO2 = new UserDetailsDTO();
        userDetailsDTO1.setEmail(user1.getEmail());
        userDetailsDTO1.setFirstName(firstName);
        userDetailsDTO1.setLastName(lastName);
        userDetailsDTO1.setBirthDate(birthdate);

        userDetailsDTO2.setEmail(user2.getEmail());
        userDetailsDTO2.setFirstName(firstName);
        userDetailsDTO2.setLastName(lastName);
        userDetailsDTO2.setBirthDate(birthdate);

        when(userDetailsMapper.toDto(
                userService.updateDetails(user1, user1.getFirstName(), user1.getLastName(), user1.getBirthDate().toString())))
                .thenReturn(userDetailsDTO1);

        mockMvc.perform(
                        patch("/users/register/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDetailsDTO1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDetailsDTO1))); // Change to userDetailsDTO2 for Fail Test
    }
}
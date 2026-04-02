package com.victor.bootcampproject.controllerTest;

import com.victor.bootcampproject.controller.implement.UserControllerImpl;
import com.victor.bootcampproject.dto.LoginDTO;
import com.victor.bootcampproject.dto.UserDetailsDTO;
import com.victor.bootcampproject.mappers.UserDetailsMapper;
import com.victor.bootcampproject.model.*;
import com.victor.bootcampproject.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@WebMvcTest(UserControllerImpl.class)
public class AppUserControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockitoBean
    private UserService userService;
    @MockitoBean private UserDetailsMapper userDetailsMapper;

    private final LocalDate birthdate = LocalDate.parse("1987-10-13");
    private final String
            firstName = "Victor",
            lastName = "Martins";

    private final AppUser user1 = new AppUser(UUID.randomUUID(),"email@mail.com");
    private final AppUser user2 = new AppUser(UUID.randomUUID(),"other@mail.com");
    private final Role role = new Role();

    @BeforeEach
    public void setUp() {
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

        when(userDetailsMapper.toDto(userService.newUser(UUID.randomUUID(),"email@mail.com"))).thenReturn(userDto1);

        mockMvc.perform(
                post("/api/users/register")
                        .with(csrf())
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
                        get("/api/users")
                                .with(csrf()))
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
                        patch("/api/users/register/details")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDetailsDTO1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDetailsDTO1))); // Change to userDetailsDTO2 for Fail Test
    }
}
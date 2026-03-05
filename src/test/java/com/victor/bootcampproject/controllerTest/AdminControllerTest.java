package com.victor.bootcampproject.controllerTest;

import com.victor.bootcampproject.controller.implement.AdminController;
import com.victor.bootcampproject.dto.UserDetailsDTO;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockitoBean
    private UserService userService;

    private final AppUser user = new AppUser("Admin@mail.de","badPassword");

    @BeforeEach public void setUp() {
    }
    @DisplayName("Test: Get All Users")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testGetAllUsers() throws Exception {
        List<AppUser> users = new ArrayList<>();
        users.add(user);
        users.add(new AppUser("User@mail.de", "badPassword"));

        when(userService.showUsers()).thenReturn(users);

        mockMvc.perform(
                get("/api/admin/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }
    @DisplayName("Test: Delete Users")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testDeleteUser() throws Exception {
        UUID uuid = UUID.randomUUID();

        UserDetailsDTO userB = new UserDetailsDTO();
        userB.setEmail(user.getEmail());

        doNothing().when(userService).deleteUserByID(uuid);
        mockMvc.perform(
                        delete("/api/admin/users/{uuid}", uuid.toString())
                                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
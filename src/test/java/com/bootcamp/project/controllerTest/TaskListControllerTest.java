package com.bootcamp.project.controllerTest;

import com.bootcamp.project.controller.implement.TaskListControllerImpl;
import com.bootcamp.project.dto.TaskListDTO;
import com.bootcamp.project.dto.TaskListTasksDTO;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.Role;
import com.bootcamp.project.model.TaskList;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.TaskListService;
import com.bootcamp.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(TaskListControllerImpl.class)
public class TaskListControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockBean private TodoListMapper taskListMapper;
    @MockBean private TaskListService taskListService;
    @MockBean private UserService userService;

    private final User user = new User("email@mail.com", "DumpPass1234");
    private final UUID userID = UUID.randomUUID();
    private final String todoListName1 = "TASK LIST TODO";
    private final String todoListName2 = "NEW TASK LIST NAME";
    private final Long todoListID = 13L;
    private final TaskListDTO taskListDTOIN = new TaskListDTO();
    private final TaskListDTO taskListDTOOUT = new TaskListDTO();
    private final TaskList taskList1 = new TaskList(todoListName1, user);
    private final TaskList taskList2 = new TaskList(todoListName2, user);

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Role role = new Role();
        role.setRoleID(1);
        role.setRole("USER");

        user.addRole(role);
        user.setUserID(userID);
        taskList1.setTodoListID(todoListID);
        taskList2.setTodoListID(todoListID);

        taskListDTOIN.setTodoListName(taskList1.getTodoListName());
        taskListDTOOUT.setTodoListName(taskList2.getTodoListName());
    }
    @DisplayName("Test: Create Task List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testCreateTaskList() throws Exception {
        when(taskListMapper.toDto(taskListService.newTaskList(user, taskList1))).thenReturn(taskListDTOIN);
        mockMvc.perform(
                post("/todolist/tasklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskListDTOIN)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskListDTOIN))); // CHange to Out for fail Test
    }
    @DisplayName("Test: Get All Task Lists")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testGetAllTaskLists() throws Exception {
        List<TaskListDTO> taskListsDTOsFail = new ArrayList<>();
        List<TaskListDTO> taskListsDTOs = new ArrayList<>();
        taskListsDTOs.add(taskListDTOIN);

        when(taskListMapper.toTaskListsDtos(taskListService.getTaskListsByUser(user))).thenReturn(taskListsDTOs);
        mockMvc.perform(
                get("/todolist/tasklist"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskListsDTOs))); // Change to taskListsDTOsFail for fail
    }
    @DisplayName("Test: Update Task List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testUpdateTaskList() throws Exception {
        TaskListTasksDTO taskListDTO1 = new TaskListTasksDTO();
        TaskListTasksDTO taskListDTO2 = new TaskListTasksDTO();
        taskListDTO1.setTodoListName(todoListName1);
        taskListDTO2.setTodoListName(todoListName2);

        when(taskListMapper.toDTO(taskListService.updateTaskList(todoListID, todoListName1))).thenReturn(taskListDTO1);
        mockMvc.perform(
                patch("/todolist/tasklist/{id}", todoListID.toString())
                        .queryParam("tklname", todoListName1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(taskListDTO1)));  // Change here to fail Test
    }
    @DisplayName("Test: Delete Task List")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testDeleteTaskList() throws Exception {
        doNothing().when(taskListService).deleteTasksList(todoListID);
        mockMvc.perform(
                delete("/todolist/tasklist/{id}", todoListID.toString()))
                .andExpect(status().isNoContent());
    }
}
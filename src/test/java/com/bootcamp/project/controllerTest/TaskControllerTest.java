package com.bootcamp.project.controllerTest;

import com.bootcamp.project.controller.implement.TaskControllerImpl;
import com.bootcamp.project.dto.TaskDTO;
import com.bootcamp.project.mappers.TaskMapper;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.Task;
import com.bootcamp.project.model.TaskList;
import com.bootcamp.project.service.TaskListService;
import com.bootcamp.project.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(TaskControllerImpl.class)
public class TaskControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;

    @MockBean private TaskListService taskListService;
    @MockBean private TaskService taskService;
    @MockBean private TaskMapper taskMapper;
    @MockBean private TodoListMapper taskListMapper;

    private final Task task1 = new Task();
    private final Task task2 = new Task();
    private final TaskList taskList = new TaskList();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Long taskID = 13L, taskListID = 13L;
        String
                taskIn = "Task1",
                taskOut = "Task2";

        task1.setTaskID(taskID);
        task2.setTaskID(taskID);
        task1.setTask(taskIn);
        task2.setTask(taskOut);

        taskList.setTodoListID(taskListID);
    }
    @DisplayName("Test: Create a Task")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testCreateTask() throws Exception {
        TaskDTO taskDto = new TaskDTO();

        taskDto.setTask(task1.getTask());
        taskDto.setDone(false);

        List<TaskDTO> taskListDtoFail = new ArrayList<>();
        List<TaskDTO> taskListDto = new ArrayList<>();
        taskListDto.add(taskDto);

        when((taskMapper.toDto(taskService.getAllTasksOfTaskList(taskList)))).thenReturn(taskListDto);
        mockMvc.perform(post("/todolist/tasklist/{id}", task1.getTaskID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(taskListDto))); // For Fail test use taskListDtoFail
    }

    @DisplayName("Test: Update a Task")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testUpdateTask() throws Exception {
        taskList.addTask(task1);

        TaskDTO taskDtoIn = new TaskDTO();
        TaskDTO taskDtoOut = new TaskDTO();

        taskDtoIn.setTask(task1.getTask());
        taskDtoIn.setDone(false);
        taskDtoOut.setTask(task2.getTask());
        taskDtoOut.setDone(false);

        when((taskMapper.toDto(taskService.updateTaskName(task1.getTaskID(), taskDtoIn.getTask())))).thenReturn(taskDtoOut);

        mockMvc.perform(
                patch("/todolist/tasklist/{tasklistID}/task/{taskID}", taskList.getTodoListID().toString(), task1.getTaskID().toString())
                        .queryParam("tname", taskDtoIn.getTask()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDtoOut)));
    }
}

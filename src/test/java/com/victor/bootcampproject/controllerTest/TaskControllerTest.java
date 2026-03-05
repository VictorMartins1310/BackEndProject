package com.victor.bootcampproject.controllerTest;

import com.victor.bootcampproject.controller.implement.TaskControllerImpl;
import com.victor.bootcampproject.dto.TaskDTO;
import com.victor.bootcampproject.mappers.TaskMapper;
import com.victor.bootcampproject.mappers.TodoListMapper;
import com.victor.bootcampproject.model.Frequency;
import com.victor.bootcampproject.model.Task;
import com.victor.bootcampproject.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.bootcampproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @MockitoBean private UserService userService;
    @MockitoBean private TaskService taskService;
    @MockitoBean private TaskMapper taskMapper;
    @MockitoBean private TodoListMapper taskListMapper;

    private final Task task1 = new Task();
    private final Task task2 = new Task();

    @BeforeEach
    public void setUp() {
        task1.setTask("Task");
        task2.setTask("Task");
    }

    @DisplayName("Test: Create a Task")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testCreateTask() throws Exception {
        TaskDTO taskDto = new TaskDTO();

        taskDto.setTask(task1.getTask());
        taskDto.setDone(false);

        task2.setTask(task1.getTask());
        task2.setTodoID(13L);
        task1.setTodoID(13L);

        //List<TaskDTO> taskListDtoFail = new ArrayList<>();
        //List<TaskDTO> taskListDto = new ArrayList<>();
        //taskListDto.add(taskDto);

        when(taskMapper.toEntity(taskDto)).thenReturn(task1);
        when(taskService.newTask(any(), eq(task1))).thenReturn(task2);
        mockMvc.perform(post("/api/todolist/tasklist")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(task2))); // For Fail test use taskListDtoFail
    }

    @DisplayName("Test: Update a Task")
    @WithMockUser(username = "testUser", roles = "USER")
    @Test public void testUpdateTask() throws Exception {
        task1.setTodoID(13L);
        TaskDTO taskDtoOut = new TaskDTO();

        String newTaskName = "NEW TASK";

        taskDtoOut.setTask(newTaskName);
        taskDtoOut.setDone(false);
        taskDtoOut.setFrequency(Frequency.Once);

        System.out.println(task1);
        System.out.println(taskDtoOut);


        when((taskMapper.toDto(taskService.updateTaskName(task1.getTodoID(), newTaskName)))).thenReturn(taskDtoOut);

        mockMvc.perform(
                patch("/api/todolist/tasklist/{tasklistID}/task/{taskID}", task1.getTodoID(), 10L)
                        .with(csrf())
                        .queryParam("tname", newTaskName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDtoOut)));
    }
}
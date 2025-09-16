package com.bootcamp.project.controller.implement;

import com.bootcamp.project.controller.TaskListController;
import com.bootcamp.project.dto.TaskListDTO;
import com.bootcamp.project.dto.TaskListTasksDTO;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.TaskList;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.TaskListService;
import com.bootcamp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** This Controller is destined for the Task List
 * It can Create, Update and Delete a Task List
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/todolist/tasklist")
public class TaskListControllerImpl implements TaskListController {
    // Services
    private final TaskListService taskListService;
    private final UserService userService;

    // Mappers
    private final TodoListMapper taskListMapper;

    // private fields
    private User loggedUser;

    /**
     * This function get the Authenticated User
     * @return Authenticated User
     */
    private User getAuthUser() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * @PostMapping Create an TaskList
     * */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListDTO newTaskList(@RequestBody TaskListDTO taskListDTO){
        loggedUser = getAuthUser();
        return taskListMapper.toDto(taskListService.newTaskList(loggedUser, new TaskList(taskListDTO.getTodoListName(), loggedUser)));
    }
    /** Show all TaskLists by that a User have */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskListDTO> showTaskLists(){
        loggedUser = getAuthUser();
        return taskListMapper.toTaskListsDtos(taskListService.getTaskListsByUser(loggedUser));
    }

    @PatchMapping(value = "/{idTasklist}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListTasksDTO updateTaskList(@PathVariable("idTasklist") Long taskListID, @RequestParam(value = "tklname") String tklname){
        return taskListMapper.toDTO(taskListService.updateTaskList(taskListID, tklname));
    }

    @DeleteMapping(value = "/{idTasklist}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskList(@PathVariable("idTasklist") Long taskListID){
        taskListService.deleteTasksList(taskListID);
    }
}
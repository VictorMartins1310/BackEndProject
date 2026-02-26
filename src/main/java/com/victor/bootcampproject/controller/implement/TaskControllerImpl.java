package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.TaskController;
import com.victor.bootcampproject.dto.TaskDTO;
import com.victor.bootcampproject.mappers.TaskMapper;
import com.victor.bootcampproject.model.Task;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.TaskService;
import com.victor.bootcampproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/** This Controller is destined for Tasks
* It can Create and Update a Task
* A delete function here is not needed,it can only be deleted by deleting the Task List
*/
@RestController
@RequiredArgsConstructor
@RequestMapping(name = "tasklist", value = "api/todolist/tasklist")
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;
    private final UserService userService;

    private final TaskMapper taskMapper;

    private AppUser loggedUser;

    private AppUser getAuthUser() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTask(@RequestBody TaskDTO taskDTO){
        loggedUser = getAuthUser();
        Task newTask = taskMapper.toEntity(taskDTO);
        return taskService.newTask(loggedUser, newTask);
    }

    @GetMapping(value = "/{taskLID}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskList(@PathVariable("taskLID") Long idTask){
        return taskService.getTask(idTask);
    }

    @PatchMapping(value = "/{taskID}/done")
    @ResponseStatus(HttpStatus.OK)
    public void markTaskCompleted(@PathVariable("taskID") Long idTask){
        taskService.markTaskCompleted(idTask);
        //return taskService.getTask(idTask);
    }

    @PatchMapping(value = "/{taskLID}/task/{taskID}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@PathVariable("taskID") Long idTask, @RequestParam("tname") String taskName){
        return taskMapper.toDto(taskService.updateTaskName(idTask, taskName));
    }
}
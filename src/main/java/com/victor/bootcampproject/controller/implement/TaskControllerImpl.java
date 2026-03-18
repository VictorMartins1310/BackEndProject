package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.TaskController;
import com.victor.bootcampproject.dto.TaskDTO;
import com.victor.bootcampproject.mappers.TaskMapper;
import com.victor.bootcampproject.model.*;
import com.victor.bootcampproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/** This Controller is destined for Tasks
* It can Create and Update a Task
* A delete function here is not needed,it can only be deleted by deleting the Task List
*/
@RestController
@RequiredArgsConstructor
@RequestMapping(name = "tasklist", value = "api/todolist/tasklist")
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;

    private final TaskMapper taskMapper;

/*
    private AppUser loggedUser;

    private AppUser getAuthUser(AppUser optionalUser) {
        if (optionalUser != null) return optionalUser;
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTask(@AuthenticationPrincipal AppUser loggedUser, @RequestBody TaskDTO taskDTO){
        Task newTask = taskMapper.toEntity(taskDTO);
        return taskService.newTask(loggedUser, newTask);
    }

    @GetMapping(value = "/{taskLID}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskList(@PathVariable("taskLID") Long idTask){
        return taskService.getTask(idTask);
    }

    @GetMapping
    public Optional<Task> getDailyTasks(@AuthenticationPrincipal AppUser loggedUser, @RequestParam("freq") Frequency freq){
        return taskService.getTasksByFrequency(loggedUser, freq);
    }

    @PatchMapping(value = "/{taskID}/done")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void markTaskCompleted(@PathVariable("taskID") Long idTask){
        taskService.markTaskCompleted(idTask);
    }

    @PatchMapping(value = "/{taskLID}/task/{taskID}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@PathVariable("taskID") Long idTask, @RequestParam("tname") String taskName){
        return taskMapper.toDto(taskService.updateTaskName(idTask, taskName));
    }
}
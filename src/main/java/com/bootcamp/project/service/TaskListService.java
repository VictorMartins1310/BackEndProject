package com.bootcamp.project.service;

import com.bootcamp.project.exception.ProjectException;
import com.bootcamp.project.model.*;
import com.bootcamp.project.repos.TaskListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskListService {
    // Repositories
    private final TaskListRepository taskListRepository;
    // Services
    private final TaskService taskService;

    public Integer countTaskListsByUser(User user){
        return taskListRepository.countTaskListsByUser(user);
    }

    public TaskList newTaskList(User user, TaskList taskList){
        return taskListRepository.save(new TaskList(taskList.getTodoListName(), user));
    }
    public List<TaskList> getTaskListsByUser(User user){
        return taskListRepository.findTaskListsByUserAndActiveIsTrue(user);
    }
    public TaskList getTaskListByID(Long id){
        if (taskListRepository.findTaskListByTodoListID(id).isEmpty())
            throw new ProjectException("Task List " + id + " Not Found");
        return taskListRepository.findTaskListByTodoListID(id).get();
    }
    public TaskList addTask2List(Long taskID, Task task){
        TaskList taskList;
        if (taskListRepository.findById(taskID).isPresent()){
            taskList = taskListRepository.findById(taskID).get();
            Task savedTask = taskService.newTask(task);
            taskList.addTask(savedTask);
            return taskListRepository.save(taskList);
        } else
            throw new ProjectException("Cannot find Task list");
    }
    public TaskList updateTaskList(Long id, String newName){
        if (taskListRepository.findTaskListByTodoListID(id).isEmpty())
            throw new ProjectException("Task List " + id + " Not Found");
        TaskList taskList = taskListRepository.findTaskListByTodoListID(id).get();
        taskList.setTodoListName(newName);
        return taskListRepository.save(taskList);
    }
    /** Function that deletes a TaskList by UserID */
    public void deleteTasksLists(User user){
        if (countTaskListsByUser(user) > 0)
            for (TaskList taskList : taskListRepository.findTaskListsByUser(user)){
                if (!taskList.getTasks().isEmpty())
                    taskService.deleteTasks(taskList.getTasks());
                deleteTasksList(taskList.getTodoListID());
            }
    }
    public void deleteTasksList(Long id){
        Optional<TaskList> taskList = taskListRepository.findById(id);
        if (taskList.isPresent())
            taskListRepository.delete(taskList.get());
    }
}
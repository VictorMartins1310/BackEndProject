package com.victor.bootcampproject.service;

import com.victor.bootcampproject.exception.ProjectException;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Task;
import com.victor.bootcampproject.repos.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    // Repositories
    private final TaskRepository taskRepository;

    public Task newTask(AppUser user, Task task){
        Task newTask = new Task(task.getTask(), task.getFrequency());
        newTask.setUser(user);
        return taskRepository.save(newTask);
    }

    public Task getTask(Long id){
        if (taskRepository.getTaskByTodoID(id).isEmpty())
            throw new ProjectException("Task Not Found");
        return taskRepository.getTaskByTodoID(id).get();
    }

    public List<Task> getTasks(AppUser user){
        return taskRepository.findByUser(user).stream().toList();
    }

    public void markTaskCompleted(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            task.get().markTaskCompleted();
            taskRepository.save(task.get());
        }
    }
    public Task updateTaskName(Long id, String name){
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty())
            throw new ProjectException("Task Not Found");
        task.get().setTask(name);
        return taskRepository.save(task.get());
    }
    public void deleteTasks(List<Task> tasks){
        //for (Task task : tasks){
            //Long taskID = task.getIdTaskList();
            //tasks.remove(task);
            //deleteTask(taskID);
        //}
    }
    public void deleteTask(Long taskID){
        var task = taskRepository.findById(taskID);
        if (task.isPresent())
            taskRepository.delete(task.get());
    }

    public List<Task> getAllTasksOfTaskList(Task taskList){
        List<Task> emptyList = new ArrayList<>();
        return emptyList;
        //return taskList.getTasks();
    }
}
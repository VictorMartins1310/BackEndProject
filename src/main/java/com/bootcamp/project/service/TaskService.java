package com.bootcamp.project.service;

import com.bootcamp.project.exception.ProjectException;
import com.bootcamp.project.model.Task;
import com.bootcamp.project.model.TaskList;
import com.bootcamp.project.repos.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    // Repositories
    private final TaskRepository taskRepository;

    public Task newTask(Task task){ return taskRepository.save(task); }
    public void taskDone(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            task.get().taskDone();
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
        for (Task task : tasks){
            Long taskID = task.getTaskID();
            tasks.remove(task);
            deleteTask(taskID);
        }
    }
    public void deleteTask(Long taskID){
        var task = taskRepository.findById(taskID);
        if (task.isPresent())
            taskRepository.delete(task.get());
    }

    public List<Task> getAllTasksOfTaskList(TaskList taskList){
        return taskList.getTasks();
    }
}
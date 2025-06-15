package com.bootcamp.project.repos;

import com.bootcamp.project.model.TaskList;
import com.bootcamp.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findTaskListsByUser(User user);
    List<TaskList> findTaskListsByUserAndActiveIsTrue(User user);
    Optional<TaskList> findTaskListByTodoListID(Long ID);
    Integer countTaskListsByUser(User user);
}
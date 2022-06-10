package com.example.todolist_prac.repository.todo;

import com.example.todolist_prac.model.todo.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    Page<TodoEntity> findBy(Pageable pageable);

    List<TodoEntity> findByCompletedFalse();
}

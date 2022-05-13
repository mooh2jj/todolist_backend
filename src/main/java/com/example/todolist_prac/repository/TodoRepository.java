package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    Page<TodoEntity> findBy(Pageable pageable);
}

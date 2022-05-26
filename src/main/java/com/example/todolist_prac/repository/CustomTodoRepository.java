package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoSearchCondition;

import java.util.List;

public interface CustomTodoRepository {

    List<TodoEntity> findAll_querydsl();

    List<TodoEntity> search(TodoSearchCondition condition);
}

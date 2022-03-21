package com.example.todolist_prac.service;

import com.example.todolist_prac.model.PageResponse;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;

import java.util.List;

public interface TodoService {

    // 추가
    TodoResponse add(TodoRequest todoRequest);

    // 조회
    TodoResponse searchById(Long id);

    // 전체 조회
    List<TodoResponse> searchAll();

    // update1
    TodoResponse updateById(Long id);

    // update2
    TodoResponse updateById(Long id, TodoRequest request);

    // 삭제
    void deleteById(Long id);

    // 전체 삭제
    void deleteAll();

    PageResponse searchAllPaging(int pageNo, int pageSize, String sortBy);
}

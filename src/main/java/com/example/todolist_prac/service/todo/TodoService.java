package com.example.todolist_prac.service.todo;

import com.example.todolist_prac.dto.PageResponse;
import com.example.todolist_prac.dto.todo.TodoRequest;
import com.example.todolist_prac.dto.todo.TodoResponse;

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

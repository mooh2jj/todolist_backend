package com.example.todolist_prac.service.todo;

import com.example.todolist_prac.exception.ErrorCode;
import com.example.todolist_prac.exception.ResourceNotFoundException;
import com.example.todolist_prac.dto.PageResponse;
import com.example.todolist_prac.model.todo.TodoEntity;
import com.example.todolist_prac.dto.todo.TodoRequest;
import com.example.todolist_prac.dto.todo.TodoResponse;
import com.example.todolist_prac.repository.todo.TodoQuerydslRepository;
import com.example.todolist_prac.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    public final TodoRepository todoRepository;

    private final TodoQuerydslRepository todoQuerydslRepository;


    @Override
    public TodoResponse add(TodoRequest todoRequest) {

        TodoEntity todoEntity = mapToEntity(todoRequest);

        TodoEntity savedTodo = todoRepository.save(todoEntity);

        return mapToDto(savedTodo);
    }

    private TodoResponse mapToDto(TodoEntity todoEntity) {

        return TodoResponse.builder()
                .id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .order(todoEntity.getOrder())
                .completed(todoEntity.getCompleted())
                .build();
    }

    // add method refactoring
    private TodoEntity mapToEntity(TodoRequest todoRequest) {

        return TodoEntity.builder()
                .title(todoRequest.getTitle())
                .order(todoRequest.getOrder())
                .completed(todoRequest.getCompleted())
                .build();
    }


    @Override
    public TodoResponse searchById(Long id) {
        var byId = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        return mapToDto(byId);
    }


    @Override
    public List<TodoResponse> searchAll() {
//        var all = todoRepository.findAll();
        List<TodoEntity> all = todoQuerydslRepository.findAll_querydsl();

        return all.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Override
    public PageResponse searchAllPaging(int pageNo, int pageSize, String sortBy) {

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TodoEntity> todoPage = todoRepository.findAll(pageable);

        // get content for page object
        List<TodoEntity> listTodos = todoPage.getContent();

        List<TodoResponse> content = listTodos.stream().map(this::mapToDto).collect(Collectors.toList());

        return PageResponse.builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(todoPage.getTotalElements())
                .totalPages(todoPage.getTotalPages())
                .last(todoPage.isLast())
                .build();

    }


    @Override
    public TodoResponse updateById(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        todoEntity.changeCompleted(true);

        return mapToDto(todoRepository.save(todoEntity));
    }


    @Override
    public TodoResponse updateById(Long id, TodoRequest request) {
        var todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));

        return mapToDto(todoRepository.save(todoEntity));
    }


    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }


    @Override
    public void deleteAll() {
        todoRepository.deleteAll();
    }



}
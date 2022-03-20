package com.example.todolist_prac.service;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    public final TodoRepository todoRepository;


    @Override
    public TodoResponse add(TodoRequest todoRequest) {

        TodoEntity todoEntity = mapToEntity(todoRequest);

        todoRepository.save(todoEntity);

        TodoResponse todoResponse = mapToDto(todoEntity);

        return todoResponse;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(byId);
    }


    @Override
    public List<TodoResponse> searchAll() {
        var all = todoRepository.findAll();

        return all.stream()
                .map(TodoEntity -> mapToDto(TodoEntity))
                .collect(Collectors.toList());

    }


    @Override
    public TodoResponse updateById(Long id) {
        var todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        todoEntity.setCompleted(true);

        return mapToDto(todoRepository.save(todoEntity));
    }


    @Override
    public TodoResponse updateById(Long id, TodoRequest request) {
        var todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (request.getTitle() != null) {
            todoEntity.setTitle(request.getTitle());
        }
        if (request.getOrder() != null) {
            todoEntity.setOrder(request.getOrder());
        }
        if (request.getCompleted() != null) {
            todoEntity.setCompleted(request.getCompleted());
        }

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

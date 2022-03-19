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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    public final TodoRepository todoRepository;

    // 추가
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

    private TodoEntity mapToEntity(TodoResponse todoResponse) {

        return TodoEntity.builder()
                .title(todoResponse.getTitle())
                .order(todoResponse.getOrder())
                .completed(todoResponse.getCompleted())
                .build();
    }

    // 조회
    public TodoResponse searchById(Long id) {
        var byId = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(byId);
    }


    // 전체 조회

    public List<TodoResponse> searchAll() {
        var all = todoRepository.findAll();

        return all.stream()
                .map(TodoEntity -> mapToDto(TodoEntity))
                .collect(Collectors.toList());

    }

    // update1
    public TodoResponse updateById(Long id) {
        var byId = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        byId.setCompleted(true);

        return mapToDto(byId);
    }

    // update2
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

    // 삭제

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    // 전체 삭제

    public void deleteAll() {
        todoRepository.deleteAll();
    }

}

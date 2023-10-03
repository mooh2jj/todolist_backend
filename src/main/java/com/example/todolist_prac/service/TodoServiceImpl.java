package com.example.todolist_prac.service;

import com.example.todolist_prac.model.PageResponse;
import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(byId);
    }


    @Override
    public List<TodoResponse> searchAll() {
        var all = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return all.stream()
                .map(TodoEntity -> mapToDto(TodoEntity))
                .collect(Collectors.toList());

    }

    @Override
    public PageResponse searchAllPaging(int pageNo, int pageSize, String sortBy) {

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TodoEntity> todoPage = todoRepository.findAll(pageable);

        // get content for page object
        List<TodoEntity> listTodos = todoPage.getContent();

        List<TodoResponse> content = listTodos.stream().map(TodoEntity -> mapToDto(TodoEntity)).collect(Collectors.toList());

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

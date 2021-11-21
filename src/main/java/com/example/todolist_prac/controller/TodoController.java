package com.example.todolist_prac.controller;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private final TodoService service;

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {
        log.info("Create");

        if (ObjectUtils.isEmpty(request.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        if (ObjectUtils.isEmpty(request.getOrder())) {
            request.setOrder(0L);
        }

        if (ObjectUtils.isEmpty(request.getCompleted())) {
            request.setCompleted(false);
        }

        TodoEntity result = this.service.add(request);

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
        log.info("Read One");
        TodoEntity result = this.service.searchById(id);

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll() {
        log.info("Read All");
        List<TodoEntity> list = this.service.searchAll();
        List<TodoResponse> responses = list.stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id) {
        log.info("Update");
        TodoEntity result = this.service.updateById(id);
        return ResponseEntity.ok(new TodoResponse(result));
    }

//    @PutMapping("{id}")
//    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
//        log.info("Update");
//        TodoEntity result = this.service.updateById(id, request);
//        return ResponseEntity.ok(new TodoResponse(result));
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        log.info("Delete");
        this.service.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        log.info("Delete All");
        this.service.deleteAll();
        return ResponseEntity.ok().build();
    }


}

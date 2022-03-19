package com.example.todolist_prac.controller;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
//@CrossOrigin
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

        TodoResponse todoResponse = this.service.add(request);

        return new ResponseEntity<>(todoResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
        log.info("Read One");
        TodoResponse todoResponse = this.service.searchById(id);

//        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
        return ResponseEntity.ok(todoResponse);
    }

    @GetMapping
    public List<TodoResponse> readAll() {
        log.info("Read All");
        return this.service.searchAll();
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id) {
        log.info("Update");
        TodoResponse todoResponse = this.service.updateById(id);
        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

/*    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> update2(@PathVariable Long id, @RequestBody TodoRequest request) {
        log.info("Update2");
        TodoResponse todoResponse = this.service.updateById(id, request);
        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }*/

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

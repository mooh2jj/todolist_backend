package com.example.todolist_prac.controller.todo;

import com.example.todolist_prac.dto.PageResponse;
import com.example.todolist_prac.dto.todo.TodoRequest;
import com.example.todolist_prac.dto.todo.TodoResponse;
import com.example.todolist_prac.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
//@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody @Valid TodoRequest request) {
        log.info("Create");

        TodoResponse todoResponse = todoService.add(request);

        return new ResponseEntity<>(todoResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
        log.info("Read One");
        TodoResponse todoResponse = todoService.searchById(id);

//        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
        return ResponseEntity.ok(todoResponse);
    }

    @GetMapping
    public List<TodoResponse> readAll() {
        log.info("Read All");
        return todoService.searchAll();
    }

    // Paging test
    @GetMapping("/page")
    public PageResponse readAllPaging(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        log.info("Read Paging All");
        return todoService.searchAllPaging(pageNo, pageSize, sortBy);
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id) {
        log.info("Update");
        TodoResponse todoResponse = todoService.updateById(id);
        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

/*    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> update2(@PathVariable Long id, @RequestBody TodoRequest request) {
        log.info("Update2");
        TodoResponse todoResponse = todoService.updateById(id, request);
        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }*/

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOne(@PathVariable Long id) {
        log.info("Delete");
        todoService.deleteById(id);
        return new ResponseEntity<>("deleteOne successfully.", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        log.info("Delete All");
        todoService.deleteAll();
        return new ResponseEntity<>("deleteAll successfully.", HttpStatus.OK);
    }


}

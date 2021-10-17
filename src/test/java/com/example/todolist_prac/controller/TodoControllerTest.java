package com.example.todolist_prac.controller;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private TodoService todoService;

    private TodoEntity expected;

    @BeforeEach
    void setup() {
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setTitle("test");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);
    }

    @AfterEach
    void afterEach() {
        todoService.deleteAll();
    }

    @Test
    void create() throws Exception {
        when(this.todoService.add(any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0, TodoRequest.class);
                    return new TodoEntity(this.expected.getId(), request.getTitle(), request.getOrder(), request.getCompleted());
                });

        TodoRequest request = new TodoRequest();
        request.setTitle(this.expected.getTitle());

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.title").value(expected.getTitle()))
                .andExpect(jsonPath("$.order").value(expected.getOrder()))
                .andExpect(jsonPath("$.completed").value(expected.getCompleted()));
    }

    @Test
    void readOne() throws Exception {
        given(todoService.searchById(123L)).willReturn(expected);

        mvc.perform(get("/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.title").value(expected.getTitle()))
                .andExpect(jsonPath("$.order").value(expected.getOrder()))
                .andExpect(jsonPath("$.completed").value(expected.getCompleted()));
    }

    @Test
    void readOneException() throws Exception {
        given(todoService.searchById(123L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get("/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void readAll() throws Exception {
        List<TodoEntity> mockList = new ArrayList<>();
        int expectedLength = 10;
        for (int i = 0; i < expectedLength; i++) {
            mockList.add(mock(TodoEntity.class));
        }

        given(todoService.searchAll()).willReturn(mockList);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedLength));
    }

    @Test
    void deleteAll() throws Exception {
        mvc.perform(delete("/"))
                .andExpect(status().isOk());
    }
}
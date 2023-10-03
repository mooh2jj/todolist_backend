package com.example.todolist_prac.controller;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.service.TodoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private TodoServiceImpl todoService;

    @Autowired
    ObjectMapper mapper;

    private TodoEntity expected;

    @BeforeEach
    void setup() {
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setTitle("test");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);
    }


    @Test
    void create() throws Exception {
        when(this.todoService.add(any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0, TodoRequest.class);
                    return new TodoResponse(this.expected.getId(), request.getTitle(), request.getOrder(), request.getCompleted());
                });

        TodoRequest request = new TodoRequest();
        request.setTitle(this.expected.getTitle());

        String content = mapper.writeValueAsString(request);

        mvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.title").value(expected.getTitle()))
                .andExpect(jsonPath("$.order").value(expected.getOrder()))
                .andExpect(jsonPath("$.completed").value(expected.getCompleted()))
                .andDo(print());
    }

    @Test
    void readOne() throws Exception {

        TodoResponse todoResponse = TodoResponse.builder()
                .id(expected.getId())
                .title(expected.getTitle())
                .order(expected.getOrder())
                .completed(expected.getCompleted())
                .build();

        given(todoService.searchById(123L)).willReturn(todoResponse);

        mvc.perform(get("/todo/{id}", 123L))
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

        mvc.perform(get("/todo/{id}", 123L))
                .andExpect(status().isNotFound());
    }

    @Test
    void readAll() throws Exception {
        int expectedLength = 10;
        List<TodoResponse> mockList = new ArrayList<>();

        TodoResponse todoResponse = TodoResponse.builder()
                .title(expected.getTitle())
                .order(expected.getOrder())
                .completed(expected.getCompleted())
                .build();

        for (int i = 0; i < expectedLength; i++) {
            mockList.add(todoResponse);
        }

        given(todoService.searchAll()).willReturn(mockList);

        mvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedLength));
    }

    @Test
    public void updateById() throws Exception {

        Long todoId = 123L;
        TodoRequest todoRequest = TodoRequest.builder()
                .title("new_title")
                .order(1L)
                .completed(true)
                .build();

        TodoResponse todoResponse = TodoResponse.builder()
                .id(todoId)
                .title(todoRequest.getTitle())
                .order(todoRequest.getOrder())
                .completed(todoRequest.getCompleted())
                .build();

//        given(todoService.updateById(todoId))     // 오류남
//                .willAnswer((v) -> v.getArgument(0));
        given(todoService.updateById(todoId)).willReturn(todoResponse);


        mvc.perform(put("/todo/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(todoResponse.getTitle())))
                .andExpect(jsonPath("$.order", CoreMatchers.is(1)))     // 1L이라 오류날수 있어 value 1 넣음
                .andExpect(jsonPath("$.completed", CoreMatchers.is(todoResponse.getCompleted())));


    }

    @Test
    public void deleteById() throws Exception {
        Long todoId = 123L;
        willDoNothing().given(todoService).deleteById(todoId);

        mvc.perform(delete("/todo/{id}", todoId))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void deleteAll() throws Exception {
        mvc.perform(delete("/todo"))
                .andExpect(status().isOk());
    }
}
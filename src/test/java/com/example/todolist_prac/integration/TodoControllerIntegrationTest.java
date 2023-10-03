package com.example.todolist_prac.integration;


import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        todoRepository.deleteAll();
    }


    @Test
    void create() throws Exception {

        TodoEntity todoEntity = TodoEntity.builder()
                .title("dsgIT")
                .order(1L)
                .completed(false)
                .build();

        TodoRequest todoRequest = TodoRequest.builder()
                .title(todoEntity.getTitle())
                .order(todoEntity.getOrder())
                .completed(todoEntity.getCompleted())
                .build();

        mvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(todoEntity.getTitle()))
                .andExpect(jsonPath("$.order").value(todoEntity.getOrder()))
                .andExpect(jsonPath("$.completed").value(todoEntity.getCompleted()))
                .andDo(print());

    }

    @Test
    void readOne() throws Exception {

        TodoEntity todoEntity = TodoEntity.builder()
                .title("dsgIT")
                .order(1L)
                .completed(false)
                .build();

        todoRepository.save(todoEntity);

        mvc.perform(get("/todo/{id}", todoEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(todoEntity.getId()))
                .andExpect(jsonPath("$.title").value(todoEntity.getTitle()))
                .andExpect(jsonPath("$.order").value(todoEntity.getOrder()))
                .andExpect(jsonPath("$.completed").value(todoEntity.getCompleted()));
    }

    @Test
    void readOneException() throws Exception {

        Long todoId = 100L;
        TodoEntity todoEntity = TodoEntity.builder()
                .title("dsgIT")
                .order(1L)
                .completed(false)
                .build();

        todoRepository.save(todoEntity);

        mvc.perform(get("/todo/{id}", todoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void readAll() throws Exception {
        int expectedLength = 10;

        IntStream.rangeClosed(1,expectedLength).forEach(i -> {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("todo_dsg"+"_"+i)
                    .order((long) i)
                    .completed(true)
                    .build();
            todoRepository.save(todoEntity);
        });

        mvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedLength));
    }

    @Test
    public void updateById() throws Exception {

        TodoEntity todoEntity = TodoEntity.builder()
                .title("dsgIT")
                .order(1L)
                .completed(false)
                .build();
        TodoEntity savedTodo = todoRepository.save(todoEntity);

        mvc.perform(put("/todo/{id}", savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(savedTodo.getTitle())))
                .andExpect(jsonPath("$.order", CoreMatchers.is(1)))     // 1L이라 오류날수 있어 value 1 넣음
                .andExpect(jsonPath("$.completed", CoreMatchers.is(true)));

    }

    @Test
    public void deleteById() throws Exception {
        TodoEntity todoEntity = TodoEntity.builder()
                .title("dsgIT")
                .order(1L)
                .completed(false)
                .build();

        TodoEntity savedTodo = todoRepository.save(todoEntity);

        mvc.perform(delete("/todo/{id}", savedTodo.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void deleteAll() throws Exception {

        int expectedLength = 10;
        List<TodoEntity> todos = new ArrayList<>();

        IntStream.rangeClosed(1,expectedLength).forEach(i -> {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("todo_dsg"+"_"+i)
                    .order((long) i)
                    .completed(true)
                    .build();
            todos.add(todoEntity);
        });
        List<TodoEntity> savedTodos = todoRepository.saveAll(todos);

        todoRepository.deleteAll(savedTodos);

        mvc.perform(delete("/todo"))
                .andExpect(status().isOk());
    }
}

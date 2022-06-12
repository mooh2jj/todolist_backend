package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.todo.TodoEntity;
import com.example.todolist_prac.dto.todo.TodoSearchCondition;
import com.example.todolist_prac.repository.todo.TodoQuerydslRepository;
import com.example.todolist_prac.repository.todo.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class TodoQueryRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoQuerydslRepository todoQuerydslRepository;

    private TodoEntity todoEntity;

    @BeforeEach
    void setup() {
        todoEntity = TodoEntity.builder()
                .title("testdsg")
                .id(1L)
                .order(10L)
                .completed(false)
                .build();
    }

    @Test
    public void given_when_then1(){
        // given - precondition or setup
        TodoEntity savedTodo = todoRepository.save(todoEntity);

        log.info("savedTodo: {}", savedTodo);
        // when - action or the behaviour that we are going test
        List<TodoEntity> all_querydsl = todoQuerydslRepository.findAll_querydsl();
        log.info("all_querydsl: {}", all_querydsl);
        // then - verify the output
        assertThat(all_querydsl.size()).isGreaterThan(1);

    }

    @Test
    public void given_when_then(){
        // given - precondition or setup
        TodoEntity savedTodo = todoRepository.save(todoEntity);

        log.info("savedTodo: {}", savedTodo);
        // when - action or the behaviour that we are going test
        TodoSearchCondition condition = new TodoSearchCondition();
        condition.setTitle("dsg");
        condition.setTodoOrder(1L);


        List<TodoEntity> search = todoQuerydslRepository.search(condition);
        // then - verify the output
        log.info("search: {}", search);

        assertThat(search.size()).isGreaterThan(1);

    }

}
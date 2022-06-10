package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
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
        // then - verify the output
        assertThat(all_querydsl.size()).isEqualTo(1);

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

        assertThat(search.size()).isEqualTo(1);

    }

}
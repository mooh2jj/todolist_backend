package com.example.todolist_prac;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestDbInit {

    private final TodoRepository todoRepository;

    @PostConstruct
    public void init() {
        log.info("init start...");
        TodoEntity todoEntity = TodoEntity.builder()
                .title("test")
                .order(1L)
                .completed(false)
                .build();
        TodoEntity todoEntity2 = TodoEntity.builder()
                .title("test2")
                .order(2L)
                .completed(false)
                .build();
        todoRepository.saveAll(Arrays.asList(todoEntity, todoEntity2));

    }


}

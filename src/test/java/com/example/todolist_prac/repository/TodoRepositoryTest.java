package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.TodoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @BeforeEach
//    @Test
    public void beforeEach() {
        System.out.println("test start");
    }

/*    @AfterEach
    void afterEach() {
        todoRepository.deleteAll();
    }*/

    @DisplayName("add 테스트")
    @Test
    public void addTest() {

        IntStream.rangeClosed(1,20).forEach(i -> {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("todo_dsg" + i)
                    .order(Long.valueOf(Integer.valueOf(i)))
                    .completed(true)
                    .build();
            todoRepository.save(todoEntity);
        });

    }

    @DisplayName("getOne 테스트")
    @Transactional
    @Test
    public void getOne() {
        var todoEntity = todoRepository.getOne(1L);
        System.out.println("todoEntity(getOne): "+ todoEntity);
    }

    @DisplayName("update 테스트")
    @Test
    public void update() {
        TodoEntity todoEntity = TodoEntity.builder()
                .id(19L)
                .title("updatedTitle")
                .order(19L)
                .completed(true)
                .build();
        System.out.println(todoRepository.save(todoEntity));

    }

    // Page

    @Test
    public void testPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<TodoEntity> result = todoRepository.findAll(pageable);
        System.out.println("result: "+ result);
    }

    @DisplayName("정렬하기 테스트")
    @Test
    public void sortTest() {
//        var todoEntities = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "order"));
//        todoEntities.forEach(System.out::println);
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<TodoEntity> result = todoRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

    @DisplayName("1개 id로 1row 찾기 테스트")
    @Test
    public void byIdTest() {
        var todoEntity = todoRepository.findById(10L);
        System.out.println("todoEntity: "+ todoEntity.orElse(null));
    }

    @DisplayName("Arrays.asList로 list 찾기 테스트")
    @Test
    public void listTest() {
        var todoEntities = todoRepository.findAllById(Arrays.asList(1L, 2L, 3L));
        todoEntities.forEach(System.out::println);

    }

}
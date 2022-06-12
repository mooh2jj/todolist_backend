package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.todo.TodoEntity;
import com.example.todolist_prac.repository.todo.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    private TodoEntity todoEntity;

    @BeforeEach
    public void setup() {
        todoEntity = TodoEntity.builder()
                .title("testTodo")
                .id(1L)
                .order(0L)
                .completed(false)
                .build();
    }

/*    @AfterEach
    void afterEach() {
        todoRepository.deleteAll();
    }*/

    @DisplayName("save 테스트")
    @Test
    public void save(){
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        TodoEntity savedTodo = todoRepository.save(todoEntity);

        log.info("savedTodo: {}", savedTodo);
        // then - verify the output
        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("testTodo");

    }


    @DisplayName("add 20개 등록 테스트")
    @Test
    public void addTest() {

        IntStream.rangeClosed(1,20).forEach(i -> {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("todo_dsg" + i)
                    .order((long) i)
                    .completed(true)
                    .build();
            todoRepository.save(todoEntity);
        });
    }

    @DisplayName("getAll 테스트")
    @Test
    public void getAll() {

        TodoEntity todoEntity1 = TodoEntity.builder()
                .title("testTodo1")
                .order(1L)
                .completed(false)
                .build();

        todoRepository.save(todoEntity);
        todoRepository.save(todoEntity1);

        List<TodoEntity> all = todoRepository.findAll();
        log.info("todoEntity(all): {}", all);

        assertThat(all).isNotNull();
//        assertThat(all.size()).isEqualTo(2);      // DB H2일시 true, 실제 DB이면 오류 날 수 있어!
    }

    @DisplayName("getById 테스트")
    @Test
    public void getById() {
        TodoEntity todoEntity = todoRepository.getById(1L);
        log.info("todoEntity(getOne): {}", todoEntity);

        assertThat(todoEntity).isNotNull();
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
        log.info("todoEntity: {}", todoRepository.save(todoEntity));

        assertThat(todoEntity.getTitle()).isEqualTo("updatedTitle");
    }

    @DisplayName("delete 테스트")
    @Test
    public void deleteById(){
        // given - precondition or setup
        TodoEntity savedTodoEntity = todoRepository.save(todoEntity);
        log.info("savedTodoEntity.getId: {}", savedTodoEntity.getId());
        // when - action or the behaviour that we are going test
        todoRepository.delete(savedTodoEntity);
//        todoRepository.deleteById(savedTodoEntity.getId());
        Optional<TodoEntity> deletedTodo = todoRepository.findById(1L);

        // then - verify the output
        assertThat(deletedTodo).isEmpty();

    }

    // Page

    @Test
    public void testPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<TodoEntity> result = todoRepository.findAll(pageable);
        log.info("result: {}", result);
    }

    @DisplayName("정렬하기 테스트")
    @Test
    public void sortTest() {

        LongStream.rangeClosed(1,20).forEach(i -> {
            TodoEntity todoEntity = TodoEntity.builder()
                    .id(i)
                    .title("todo_dsg" + i)
                    .order(i)
                    .completed(true)
                    .build();
            todoRepository.save(todoEntity);
        });

        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<TodoEntity> result = todoRepository.findAll(pageable);
        log.info("result: {}", result.getContent());

        result.get().forEach(System.out::println);
    }

    @DisplayName("1개 id로 1row 찾기 테스트")
    @Test
    public void byIdTest() {
        var todoEntity = todoRepository.findById(10L);
        log.info("todoEntity: {}", todoEntity.orElse(null));
    }

    @DisplayName("Arrays.asList로 list 찾기 테스트")
    @Test
    public void listTest() {

        List<TodoEntity> allById = todoRepository.findAllById(Arrays.asList(1L, 2L, 3L));
        allById.forEach(System.out::println);

    }

    @Test
    public void findByCompletedFalse() {
        List<TodoEntity> byCompletedFalse = todoRepository.findByCompletedFalse();
        byCompletedFalse.forEach(System.out::println);
    }

}
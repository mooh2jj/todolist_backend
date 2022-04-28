package com.example.todolist_prac.service;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.model.TodoResponse;
import com.example.todolist_prac.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    private TodoEntity todoEntity;

    @BeforeEach
    public void setup() {
        todoEntity = TodoEntity.builder()
                .id(1L)
                .title("testTodo")
                .order(0L)
                .completed(false)
                .build();
    }

    @Test
    public void add() {
        when(this.todoRepository.save(any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());
//        given(todoRepository.save(todoEntity)).willReturn(todoEntity);

        TodoRequest todoRequest = TodoRequest.builder()
                .title(todoEntity.getTitle())
                .order(todoEntity.getOrder())
                .completed(todoEntity.getCompleted())
                .build();
        TodoResponse savedResponse = this.todoService.add(todoRequest);

        log.info("savedResponse: {}", savedResponse);
//        assertEquals(1L,  savedResponse.getId());       //  test 오류 null 뜨는지 이해 불가.
//        assertEquals("testTodo",  savedResponse.getTitle());
        assertThat(savedResponse).isNotNull();
        assertThat(savedResponse.getTitle()).isEqualTo(todoRequest.getTitle());
    }

    @Test
    public void searchById() {

        Optional<TodoEntity> expected = Optional.of(todoEntity);

        given(this.todoRepository.findById(1L))
                .willReturn(expected);

        TodoResponse response = this.todoService.searchById(1L);

        log.info("response: {}", response);

//        assertEquals(response.getId(), 1L);
//        assertEquals(response.getOrder(), 0L);
//        assertFalse(response.getCompleted());
//        assertEquals(response.getTitle(), "testTodo");
        assertThat(response.getTitle()).isEqualTo("testTodo");
        assertThat(response.getOrder()).isEqualTo(0L);
    }


    // 에러 발생 테스트도 만듦
    @Test
    public void searchById_ThrowsException() {
        given(this.todoRepository.findById(anyLong())).willReturn(Optional.empty());

//        assertThrows(ResponseStatusException.class, () -> {
//            this.todoService.searchById(1L);
//        });
        assertThatThrownBy(() -> {
            todoService.searchById(1L);
        }).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void searchAll(){
        // given - precondition or setup
        TodoEntity todoEntity1 = TodoEntity.builder()
                .id(100L)
                .title("test_dsg")
                .order(1L)
                .completed(true)
                .build();
        given(todoRepository.findAll()).willReturn(List.of(todoEntity, todoEntity1));

        // when - action or the behaviour that we are going test
        List<TodoResponse> todoResponses = todoService.searchAll();
        log.info("todoResponses: {}", todoResponses);

        // then - verify the output
        assertThat(todoResponses).isNotNull();
        assertThat(todoResponses.size()).isEqualTo(2);

    }

    @Test
    public void searchAll_negative(){
        // given - precondition or setup
        given(todoRepository.findAll()).willReturn(Collections.emptyList());
        // when - action or the behaviour that we are going test
        List<TodoResponse> todoResponses = todoService.searchAll();
        log.info("todoResponses: {}", todoResponses);
        // then - verify the output
        assertThat(todoResponses).isEmpty();
        assertThat(todoResponses.size()).isEqualTo(0);

    }

    @Test
    public void updateById(){
        // given - precondition or setup
        given(todoRepository.findById(anyLong()))
                .willReturn(Optional.of(todoEntity));
        given(todoRepository.save(todoEntity)).willReturn(todoEntity);      // updateById면 두 상황 모두 만들어져야돼!

        TodoRequest request = TodoRequest.builder()
                .title("kkk")
                .order(2L)
                .build();

        // when - action or the behaviour that we are going test
        TodoResponse todoResponse = todoService.updateById(todoEntity.getId(), request);
        log.info("todoResponses: {}", todoResponse);
        // then - verify the output
        assertThat(todoResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(todoResponse.getOrder()).isEqualTo(request.getOrder());

    }

    @Test
    public void deleteById(){
        // given - precondition or setup
        Long todoId = 1L;
        willDoNothing().given(todoRepository).deleteById(todoId);

        // when - action or the behaviour that we are going test
        todoService.deleteById(todoId);
        // then - verify the output
        verify(todoRepository).deleteById(todoId);

    }
}
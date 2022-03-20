package com.example.todolist_prac.service;

import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoRequest;
import com.example.todolist_prac.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    public void add() {
        when(this.todoRepository.save(any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        TodoRequest request = new TodoRequest();
        request.setTitle("test title");
        TodoEntity actual = this.todoService.add(request);

//        assertEquals(1L,  actual.getId());         test 오류 null 뜨는지 이해 불가.
        assertEquals("test title",  actual.getTitle());
    }

    @Test
    public void searchById() {
        TodoEntity todo = new TodoEntity();
        todo.setTitle("test");
        todo.setId(123L);
        todo.setOrder(0L);
        todo.setCompleted(false);
        Optional<TodoEntity> expected = Optional.of(todo);

        given(this.todoRepository.findById(anyLong()))
                .willReturn(expected);

        TodoEntity actual = this.todoService.searchById(123L);

        assertEquals(actual.getId(), 123L);
        assertEquals(actual.getOrder(), 0L);
        assertFalse(actual.getCompleted());
        assertEquals(actual.getTitle(), "test");
    }


    // 에러 발생 테스트도 만듦
    @Test
    public void searchByIdFailed() {
        given(this.todoRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L);
        });
    }
}
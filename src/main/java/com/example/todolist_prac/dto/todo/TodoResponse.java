package com.example.todolist_prac.dto.todo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodoResponse {

    private Long id;

    private String title;

    private Long order;

    private Boolean completed;

    @Builder
    public TodoResponse(Long id, String title, Long order, Boolean completed) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.completed = completed;
    }
}

package com.example.todolist_prac.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodoNotificationDto {

    private Long id;

    private String title;

    private Long order;

    private Boolean completed;

    private int count;

    @Builder
    public TodoNotificationDto(Long id, String title, Long order, Boolean completed, int count) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.completed = completed;
        this.count = count;
    }
}

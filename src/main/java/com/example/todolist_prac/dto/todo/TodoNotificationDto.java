package com.example.todolist_prac.dto.todo;

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

    private Integer count;

    @Builder
    public TodoNotificationDto(Long id, String title, Long order, Boolean completed, Integer count) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.completed = completed;
        this.count = count;
    }

    public String toMessage() {
        return String.format("%s todo 알림\n" +
                "총 %d todo가 있습니다.\n", "dsg님", count)
                +
                String.format("title: %s\n" +
                        "order: %d\n" +
                        "completed: %s\n", title, order, completed);
    }
}

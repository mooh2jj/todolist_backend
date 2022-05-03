package com.example.todolist_prac.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Todo")
public class TodoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "todoOrder", nullable = false)
    private Long order;

    @Column(nullable = false)
    private Boolean completed;

    @Builder
    public TodoEntity(Long id, String title, Long order, Boolean completed) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.completed = completed;
    }

    public void changeCompleted(boolean completed) {
        this.completed = completed;
    }
}

package com.example.todolist_prac.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {

    private Long id;

    private String title;

    private Long order;

    private Boolean completed;

}

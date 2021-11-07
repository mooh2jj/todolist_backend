package com.example.todolist_prac.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {

    private String title;

    private Long order;

    private Boolean completed;


}


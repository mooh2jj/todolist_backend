package com.example.todolist_prac.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Order;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

    private String title;

    private Long order;

    private Boolean completed;

}


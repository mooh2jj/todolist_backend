package com.example.todolist_prac.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {

    private List<TodoResponse> content;
    private int pageNo;
    private int pageSize;

}

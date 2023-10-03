package com.example.todolist_prac.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {

    @NotEmpty
    @Size(min = 2, message = "제목은 2글자 이상이어야 합니다.")
    private String title;

    private Long order;

    private Boolean completed;


}


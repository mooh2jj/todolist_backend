package com.example.todolist_prac.dto.user;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginDto {

    private String usernameOrEmail;
    private String password;
}

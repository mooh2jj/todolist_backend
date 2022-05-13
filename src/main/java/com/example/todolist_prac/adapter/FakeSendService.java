package com.example.todolist_prac.adapter;

import org.springframework.stereotype.Service;

@Service
public class FakeSendService implements SendService{

    @Override
    public void send(String title, String completed) {
        System.out.println("title: " + title + "\ncompleted: \n" + completed);
    }
}

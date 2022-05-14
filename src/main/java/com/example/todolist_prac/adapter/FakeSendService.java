package com.example.todolist_prac.adapter;

import org.springframework.stereotype.Service;

@Service
public class FakeSendService implements SendService{

    @Override
    public void send(String title, String message) {
        System.out.println("title: " + title + "\nmessage: \n" + message);
    }
}

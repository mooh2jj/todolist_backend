package com.example.todolist_prac.controller;

import com.example.todolist_prac.components.MailComponents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailComponents mailComponents;

    @GetMapping("/mail")
    public void mail() {
        String email = "ehtjd33@gmail.com";
        String subject = "안녕하세요. todo service입니다.";
        String text = "<p>안녕하세요.</p> <p>todo service 입니다. 반갑습니다.</p> ";
        mailComponents.sendMail(email, subject, text);
        log.info("mailComponents.sendMail();");
    }
}

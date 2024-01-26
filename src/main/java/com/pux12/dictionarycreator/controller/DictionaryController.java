package com.pux12.dictionarycreator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}

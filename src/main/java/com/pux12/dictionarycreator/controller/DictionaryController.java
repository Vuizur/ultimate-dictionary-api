package com.pux12.dictionarycreator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pux12.dictionarycreator.model.Etymology;
import com.pux12.dictionarycreator.service.EtymologyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class DictionaryController {
    @Autowired
    private EtymologyService etymologyService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("word/{word}")
    public Etymology findByWord(@PathVariable String word) {
        return etymologyService.findByWord(word);
    }

}

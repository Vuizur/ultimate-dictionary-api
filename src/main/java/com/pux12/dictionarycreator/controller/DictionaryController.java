package com.pux12.dictionarycreator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pux12.dictionarycreator.model.Etymology;
import com.pux12.dictionarycreator.repository.EtymologyRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

@RestController
public class DictionaryController {
    @Autowired
    private EtymologyRepository etymologyRepository;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("word/{word}")
    public ResponseEntity<String> findByWord(@PathVariable String word) {
        return ResponseEntity.ok(etymologyRepository.findByWrd(word));
    }

    @RequestMapping("source/{source_wiktionary_code}")
    public Page<Etymology> findBySourceWiktionaryCode(@PathVariable String source_wiktionary_code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        return etymologyRepository.findBySourceWiktionaryCode(source_wiktionary_code, PageRequest.of(page, size));
    }

    @RequestMapping("random/{word}")
    public Etymology findRandom(@PathVariable String word) {
        return etymologyRepository.testRandom(word);
    }

    @RequestMapping("translation/{sourceLangCode}/{targetLangCode}/{word}")
    public List<String> findTranslation(@PathVariable String sourceLangCode, @PathVariable String targetLangCode,
            @PathVariable String word) {
        return etymologyRepository.findTranslation(sourceLangCode, targetLangCode, word);
    }
}

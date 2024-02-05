package com.pux12.dictionarycreator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pux12.dictionarycreator.model.dto.WordDTO;
import com.pux12.dictionarycreator.model.entity.Etymology;
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

    ObjectMapper mapper = new ObjectMapper();

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

    @RequestMapping("translation/{sourceLangCode}/{targetLangCode}/{word}")
    public String findTranslation(@PathVariable String sourceLangCode, @PathVariable String targetLangCode,
            @PathVariable String word) {

        try {
            ObjectNode result = mapper.createObjectNode();
            String properWiktionaryEntries = etymologyRepository.findProperWiktionaryEntries(sourceLangCode,
            targetLangCode, word);
            var translationWithIPA = etymologyRepository.findTranslationWithPosAndIPA(sourceLangCode, targetLangCode, word);
            System.out.println(translationWithIPA);
            String contextLessTranslation = etymologyRepository.findContextLessTranslation(sourceLangCode,
                    targetLangCode, word);

            if (properWiktionaryEntries != null) {
                result.set("entries", mapper.readTree(properWiktionaryEntries));
            }
            if (translationWithIPA != null) {
                result.set("posTranslations", mapper.readTree(translationWithIPA));
            }
            if (contextLessTranslation != null) {
                result.set("translations", mapper.readTree(contextLessTranslation));
            }

            return result.toString();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @RequestMapping("propertranslation/{sourceLangCode}/{targetLangCode}/{word}")
    public String findProperTranslation(@PathVariable String sourceLangCode, @PathVariable String targetLangCode,
            @PathVariable String word) {
        return etymologyRepository.findProperWiktionaryEntries(sourceLangCode, targetLangCode, word);
    }
}

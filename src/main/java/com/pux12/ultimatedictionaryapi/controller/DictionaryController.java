package com.pux12.ultimatedictionaryapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pux12.ultimatedictionaryapi.model.entity.Etymology;
import com.pux12.ultimatedictionaryapi.repository.EtymologyRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        return "Welcome to the Ultimate Dictionary API!";
    }

    @RequestMapping(value = "word/{word}", method = RequestMethod.GET)
    public ResponseEntity<String> findByWord(@PathVariable String word) {
        return ResponseEntity.ok(etymologyRepository.findByWrd(word));
    }

    @RequestMapping(value = "source/{source_wiktionary_code}", method = RequestMethod.GET)
    public Page<Etymology> findBySourceWiktionaryCode(@PathVariable String source_wiktionary_code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        return etymologyRepository.findBySourceWiktionaryCode(source_wiktionary_code, PageRequest.of(page, size));
    }

    @RequestMapping(value = "translation/{sourceLangCode}/{targetLangCode}/{word}", method = RequestMethod.GET)
    public String findTranslation(@PathVariable String sourceLangCode, @PathVariable String targetLangCode,
            @PathVariable String word) {

        try {
            ObjectNode result = mapper.createObjectNode();
            String properWiktionaryEntries = etymologyRepository.findProperWiktionaryEntries(sourceLangCode,
                    targetLangCode, word);
            var translationWithIPA = etymologyRepository.findTranslationWithPosAndIPA(sourceLangCode, targetLangCode,
                    word);
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

    @RequestMapping(value = "propertranslation/{sourceLangCode}/{targetLangCode}/{word}", method = RequestMethod.GET)
    public String findProperTranslation(@PathVariable String sourceLangCode, @PathVariable String targetLangCode,
            @PathVariable String word) {
        return etymologyRepository.findProperWiktionaryEntries(sourceLangCode, targetLangCode, word);
    }
}

package com.pux12.dictionarycreator.model.dto;

import java.util.List;

public class TranslationDTO {
    private List<Word> words;
    private List<String> unstructuredTranslations;
    public TranslationDTO(List<Word> words, List<String> unstructuredTranslations) {
        this.words = words;
        this.unstructuredTranslations = unstructuredTranslations;
    }
    public List<Word> getWords() {
        return words;
    }
    public void setWords(List<Word> words) {
        this.words = words;
    }
    public List<String> getUnstructuredTranslations() {
        return unstructuredTranslations;
    }
    public void setUnstructuredTranslations(List<String> unstructuredTranslations) {
        this.unstructuredTranslations = unstructuredTranslations;
    }
}

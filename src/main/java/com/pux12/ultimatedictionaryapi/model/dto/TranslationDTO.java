package com.pux12.ultimatedictionaryapi.model.dto;

import java.util.List;

public class TranslationDTO {
    private List<WordDTO> words;
    private List<String> unstructuredTranslations;
    public TranslationDTO(List<WordDTO> words, List<String> unstructuredTranslations) {
        this.words = words;
        this.unstructuredTranslations = unstructuredTranslations;
    }
    public List<WordDTO> getWords() {
        return words;
    }
    public void setWords(List<WordDTO> words) {
        this.words = words;
    }
    public List<String> getUnstructuredTranslations() {
        return unstructuredTranslations;
    }
    public void setUnstructuredTranslations(List<String> unstructuredTranslations) {
        this.unstructuredTranslations = unstructuredTranslations;
    }
}

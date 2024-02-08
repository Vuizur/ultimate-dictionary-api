package com.pux12.ultimatedictionaryapi.model.dto;

import java.util.List;

public class TranslationWithPosAndIPADTO {
    private String word;
    private String pos;
    private List<String> ipa;
    private List<String> translations;

    public TranslationWithPosAndIPADTO(String word, String pos, List<String> ipa, List<String> translations) {
        this.word = word;
        this.pos = pos;
        this.ipa = ipa;
        this.translations = translations;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<String> getIpa() {
        return ipa;
    }

    public void setIpa(List<String> ipa) {
        this.ipa = ipa;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

}

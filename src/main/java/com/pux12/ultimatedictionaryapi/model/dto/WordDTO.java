package com.pux12.ultimatedictionaryapi.model.dto;

import java.util.List;

public class WordDTO {
    private String word;
    private List<String> ipa;
    private String pos;
    private String etymology;
    private List<SenseDTO> senses;

    public WordDTO(String word, List<String> ipa, String pos, String etymology, List<SenseDTO> senses) {
        this.word = word;
        this.ipa = ipa;
        this.pos = pos;
        this.etymology = etymology;
        this.senses = senses;
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

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public List<SenseDTO> getSenses() {
        return senses;
    }

    public void setSenses(List<SenseDTO> senses) {
        this.senses = senses;
    }

    public List<String> getIpa() {
        return ipa;
    }

    public void setIpa(List<String> ipa) {
        this.ipa = ipa;
    }
}

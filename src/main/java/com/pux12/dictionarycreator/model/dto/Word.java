package com.pux12.dictionarycreator.model.dto;

import java.util.List;

public class Word {
    private String word;
    private String ipa;
    private String pos;
    private String etymology;
    private List<Sense> senses;

    public Word(String word, String ipa, String pos, String etymology, List<Sense> senses) {
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

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
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

    public List<Sense> getSenses() {
        return senses;
    }

    public void setSenses(List<Sense> senses) {
        this.senses = senses;
    }
}

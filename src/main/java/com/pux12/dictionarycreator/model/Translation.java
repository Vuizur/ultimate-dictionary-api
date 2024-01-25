package com.pux12.dictionarycreator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Translation {
    @ManyToOne
    @JoinColumn(name = "etymology_id")
    private Etymology etymology;

    // Index
    private String word;

    // Index
    private String lang_code;

    // Index
    private String sense;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    @Override
    public String toString() {
        return "Translation [word=" + word + ", lang_code=" + lang_code + ", sense=" + sense + "]";
    }

    public Etymology getEtymology() {
        return etymology;
    }

    public void setEtymology(Etymology etymology) {
        this.etymology = etymology;
    }
}

package com.pux12.dictionarycreator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etymology_id")
    @JsonIgnore
    private Etymology etymology;

    // Index
    private String word;

    // Index
    private String lang_code;

    // Index
    @Column(columnDefinition = "text")
    private String sense;

    public Translation(String word, String lang_code, String sense) {
        this.word = word;
        this.lang_code = lang_code;
        this.sense = sense;
    }

    public Translation() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

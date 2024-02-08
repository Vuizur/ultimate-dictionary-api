package com.pux12.ultimatedictionaryapi.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etymology_id")
    @JsonIgnore
    private Etymology etymology;

    @Column(columnDefinition = "text")
    private String word;

    private String lang_code;

    private String lang;

    @Column(columnDefinition = "text")
    private String sense;

    @ElementCollection
    @Column(columnDefinition = "text") // Otherwise crashes
    private List<String> tags;

    @ElementCollection
    private List<String> senseIds;

    public Translation(String word, String lang_code, String lang, String sense, List<String> tags,
            List<String> senseIds) {
        this.word = word;
        this.lang_code = lang_code;
        this.lang = lang;
        this.sense = sense;
        this.tags = tags;
        this.senseIds = senseIds;
    }

    public Translation() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public List<String> getSenseIds() {
        return senseIds;
    }

    public void setSenseIds(List<String> senseIds) {
        this.senseIds = senseIds;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

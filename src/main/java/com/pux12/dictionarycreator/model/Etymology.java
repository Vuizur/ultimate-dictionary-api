package com.pux12.dictionarycreator.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "etymology")
public class Etymology {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition="text")
    private String word;

    @Column(columnDefinition="text")
    private String pos;

    private String langCode;

    @Column(columnDefinition="text")
    private String etymology;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Form> forms;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Sense> senses;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Translation> translations;

    private String sourceWiktionaryCode;

    public Etymology(String word, String pos, String langCode, String etymology, String sourceWiktionaryCode) {
        this.word = word;
        this.pos = pos;
        this.langCode = langCode;
        this.etymology = etymology;
        this.sourceWiktionaryCode = sourceWiktionaryCode;
    }

    public Etymology() {
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public void setSenses(List<Sense> senses) {
        this.senses = senses;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
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

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getSourceWiktionaryCode() {
        return sourceWiktionaryCode;
    }

    public void setSourceWiktionaryCode(String sourceWiktionaryCode) {
        this.sourceWiktionaryCode = sourceWiktionaryCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return "Etymology [id=" + id + ", word=" + word + ", pos=" + pos + ", langCode=" + langCode + ", etymology="
                + etymology + ", forms=" + forms + ", senses=" + senses + ", translations=" + translations
                + ", sourceWiktionaryCode=" + sourceWiktionaryCode + "]";
    }

}

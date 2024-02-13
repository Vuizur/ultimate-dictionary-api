package com.pux12.ultimatedictionaryapi.model.entity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "text")
    private String word;

    @Column(columnDefinition = "text")
    private String pos;

    private String lang;

    private String langCode;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Sound> sounds;

    @Column(columnDefinition = "text")
    private String etymology;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Form> forms;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Sense> senses;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Synonym> synonyms;

    @OneToMany(mappedBy = "etymology", cascade = CascadeType.ALL)
    private List<Translation> translations;

    private String sourceWiktionaryCode;

    private Float randomNumber;

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

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Float getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Float randomNumber) {
        this.randomNumber = randomNumber;
    }

}

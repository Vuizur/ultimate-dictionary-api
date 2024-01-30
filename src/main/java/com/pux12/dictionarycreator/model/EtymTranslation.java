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
public class EtymTranslation extends Translation {
    @ManyToOne
    @JoinColumn(name = "etymology_id")
    @JsonIgnore
    private Etymology etymology;

    @Column(columnDefinition = "text")
    private String senseString;

    public EtymTranslation(String word, String code, String lang, String senseString) {
        super(word, code, lang);
        this.senseString = senseString;
    }

    public EtymTranslation() {
    }

    public Etymology getEtymology() {
        return etymology;
    }

    public void setEtymology(Etymology etymology) {
        this.etymology = etymology;
    }

    public String getSenseString() {
        return senseString;
    }

    public void setSenseString(String senseString) {
        this.senseString = senseString;
    }
}

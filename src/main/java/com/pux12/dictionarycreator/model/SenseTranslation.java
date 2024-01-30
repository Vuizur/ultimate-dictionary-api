package com.pux12.dictionarycreator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SenseTranslation extends Translation {
    @ManyToOne
    @JoinColumn(name = "sense_id")
    private Sense sense;

    public SenseTranslation(String word, String code, String lang) {
        super(word, code, lang);
    }

    public SenseTranslation() {
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

}
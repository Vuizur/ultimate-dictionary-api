package com.pux12.dictionarycreator.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Sense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etymology_id")
    private Etymology etymology;

    // Simplified, in the original there is also
    @ElementCollection
    private List<String> examples;

    // We probably need an additional Gloss Entity
    @ElementCollection
    private List<String> glosses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public List<String> getGlosses() {
        return glosses;
    }

    public void setGlosses(List<String> glosses) {
        this.glosses = glosses;
    }

    @Override
    public String toString() {
        return "Sense [examples=" + examples + ", glosses=" + glosses + "]";
    }

    public Etymology getEtymology() {
        return etymology;
    }

    public void setEtymology(Etymology etymology) {
        this.etymology = etymology;
    }

}

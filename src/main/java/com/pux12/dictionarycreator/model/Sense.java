package com.pux12.dictionarycreator.model;

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
public class Sense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etymology_id")
    @JsonIgnore
    private Etymology etymology;
    // We probably need an additional Gloss Entity
    @ElementCollection
    @Column(columnDefinition = "text")
    private List<String> glosses;
    // Simplified, in the original there is also more info
    @ElementCollection
    @Column(columnDefinition = "text")
    private List<String> examples;

    public Sense(List<String> glosses, List<String> examples) {
        this.glosses = glosses;
        this.examples = examples;
    }

    public Sense() {
    }

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

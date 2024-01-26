package com.pux12.dictionarycreator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etymology_id")
    @JsonIgnore
    private Etymology etymology;

    @Column(columnDefinition="text")
    private String form;

    @ElementCollection
    private List<String> tags;

    public Form(String form, List<String> tags) {
        this.form = form;
        this.tags = tags;
    }

    public Form() {
    }

    public Etymology getEtymology() {
        return etymology;
    }

    public void setEtymology(Etymology etymology) {
        this.etymology = etymology;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Form [form=" + form + ", tags=" + tags + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

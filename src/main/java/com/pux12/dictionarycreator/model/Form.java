package com.pux12.dictionarycreator.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Form {
    @ManyToOne
    @JoinColumn(name = "etymology_id")
    private Etymology etymology;

    private String form;
    
    @ElementCollection
    private List<String> tags;

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
    
}

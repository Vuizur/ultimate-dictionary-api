package com.pux12.dictionarycreator.model.dto;

import java.util.List;

public class Sense {
    private List<String> glosses;
    private List<String> examples;

    public Sense(List<String> glosses, List<String> examples) {
        this.glosses = glosses;
        this.examples = examples;
    }

    public List<String> getGlosses() {
        return glosses;
    }

    public void setGlosses(List<String> glosses) {
        this.glosses = glosses;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }
}

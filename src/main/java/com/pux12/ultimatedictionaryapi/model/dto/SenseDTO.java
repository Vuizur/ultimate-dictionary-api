package com.pux12.ultimatedictionaryapi.model.dto;

import java.util.List;

public class SenseDTO {
    private List<String> glosses;
    private List<String> examples;

    public SenseDTO(List<String> glosses, List<String> examples) {
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

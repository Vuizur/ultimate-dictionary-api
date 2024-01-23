package com.pux12.dictionarycreator.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "etymology")
public class Etymology {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String word;
    private String pos;
    private String langCode;
    private String etymology;
    private String senseJSON;
    private String translationsJSON;
    private String sourceWiktionaryCode;

    
}

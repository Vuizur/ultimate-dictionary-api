package com.pux12.dictionarycreator.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.pux12.dictionarycreator.model.Etymology;
import com.pux12.dictionarycreator.model.Form;
import com.pux12.dictionarycreator.model.Sense;
import com.pux12.dictionarycreator.model.Translation;
import com.pux12.dictionarycreator.repository.EtymologyRepository;

import jakarta.annotation.PostConstruct;

@Service
public class EtymologyService {
    @Autowired
    private EtymologyRepository etymologyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Etymology findByWord(String word) {
        return etymologyRepository.findByWord(word);
    }

    @PostConstruct
    public void insertData() {
        System.out.println("Inserting data");
        var res = jdbcTemplate.query(
                "select true from etymology limit 1;", (resultSet, rowNum) -> resultSet.getBoolean(1));
        if (!res.isEmpty()) {
            System.out.println("Data already inserted");
        } else {
            System.out.println("Inserting data");
            // var forms = new ArrayList<Form>();
            // var tags = new ArrayList<String>();
            // tags.add("gerund");
            // tags.add("past participle");
            // forms.add(new Form("tested", tags));
            // forms.add(new Form("testing", null));
            // var glosses = new ArrayList<String>();
            // glosses.add("to perform a test");
            // var senses = new ArrayList<Sense>();
            // senses.add(new Sense(glosses, null));
            // Etymology etymology = new Etymology("test", "noun", "en", "born from
            // nothing", forms, senses, null, "en");
            // etymologyRepository.save(etymology);
        }
    }
}

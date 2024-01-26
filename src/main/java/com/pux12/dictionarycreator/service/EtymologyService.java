package com.pux12.dictionarycreator.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pux12.dictionarycreator.datainsert.WiktextractDownloader;
import com.pux12.dictionarycreator.model.Etymology;
import com.pux12.dictionarycreator.model.Form;
import com.pux12.dictionarycreator.model.Sense;
import com.pux12.dictionarycreator.model.Translation;
import com.pux12.dictionarycreator.repository.EtymologyRepository;
import com.pux12.dictionarycreator.repository.FormRepository;
import com.pux12.dictionarycreator.repository.SenseRepository;
import com.pux12.dictionarycreator.repository.TranslationRepository;

import jakarta.annotation.PostConstruct;

@Service
public class EtymologyService {
    @Autowired
    private EtymologyRepository etymologyRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private SenseRepository senseRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Etymology findByWord(String word) {
        return etymologyRepository.findByWord(word);
    }

    public void insertDataFromWiktionary() {
        ObjectMapper mapper = new ObjectMapper();
        WiktextractDownloader downloader = new WiktextractDownloader();

        // Benchmark the time it takes to insert all the data
        long startTime = System.currentTimeMillis();

        int totalInserts = 0;

        try {
            // For each dump, print the first 10 lines
            for (var dumpPath : downloader.getWiktionaryDumpPaths()) {

                var dumpFile = new File(dumpPath);
                // if dumpfile doesn't contain "ru" then continue
                // if (!dumpFile.getName().contains("ru")) {
                // continue;
                // }
                var dumpReader = new BufferedReader(new FileReader(dumpFile));

                String sourceWiktionaryCode = null;
                if (dumpFile.getName().contains("de-extract")) {
                    sourceWiktionaryCode = "de";
                } else if (dumpFile.getName().contains("es-extract")) {
                    sourceWiktionaryCode = "es";
                } else if (dumpFile.getName().contains("fr-extract")) {
                    sourceWiktionaryCode = "fr";
                } else if (dumpFile.getName().contains("ru-extract")) {
                    sourceWiktionaryCode = "ru";
                } else if (dumpFile.getName().contains("zh-extract")) {
                    sourceWiktionaryCode = "zh";
                } else if (dumpFile.getName().contains("raw-wiktextract-data")) {
                    sourceWiktionaryCode = "en";
                } else {
                    System.out.println("Error: no source wiktionary code found for " + dumpFile.getName());
                    System.exit(1);
                }

                System.out.println("#########");
                System.out.println(dumpFile.getName());
                int i = 0;
                String line = null;
                while ((line = dumpReader.readLine()) != null) {

                    if (i > 500) {
                        break;
                    }
                    var json = mapper.readTree(line);

                    String word = null;
                    String pos = null;
                    String langCode = null;
                    String etym = null;
                    var senses = new ArrayList<Sense>();
                    //String translations = null;

                    // Get the word if it exists
                    if (json.has("word")) {
                        word = json.get("word").asText();
                    } else {
                        // This is mostly redirects, which we for now ignore
                        continue;
                    }
                    if (json.has("pos")) {
                        pos = json.get("pos").asText();
                    }
                    if (json.has("etymology_text")) {
                        etym = json.get("etymology_text").asText();
                    }
                    if (json.has("lang_code")) {
                        langCode = json.get("lang_code").asText();
                    }
                    if (json.has("senses")) {
                        // Iterate over the senses
                        for (var senseJson : json.get("senses")) {
                            var glosses = new ArrayList<String>();
                            var examples = new ArrayList<String>();
                            if (senseJson.has("glosses")) {
                                for (var glossJson : senseJson.get("glosses")) {
                                    glosses.add(glossJson.asText());
                                }
                            }
                            if (senseJson.has("examples")) {
                                for (var exampleText : senseJson.get("examples")) {
                                    // TODO: Make it proper, examples are actually more complicated than this
                                    if (exampleText.has("text")) {
                                        examples.add(exampleText.get("text").asText());
                                    }
                                }
                            }
                            var sense = new Sense(glosses, examples);
                            senses.add(sense);
                        }
                    }
                    // Insert using JPA
                    var etymology = new Etymology(word, pos, langCode, etym, sourceWiktionaryCode);
                    etymologyRepository.save(etymology);

                    // Insert the senses
                    senses.forEach(sense -> sense.setEtymology(etymology));
                    senses.forEach(sense -> senseRepository.save(sense));
                    totalInserts++;
                    i++;
                }

                dumpReader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        // Print with HH:MM:SS.MS format
        System.out.println("Inserting data took " + String.format("%02d:%02d:%02d.%04d",
                (endTime - startTime) / 3600000, ((endTime - startTime) / 60000) % 60,
                ((endTime - startTime) / 1000) % 60, (endTime - startTime) % 1000));

        // Print insertions by seconds
        System.out.println("Inserting data took " + totalInserts / ((endTime - startTime) / 1000) + " inserts/s");
    }

    public void insertTestEtymology() {
        var forms = new ArrayList<Form>();
        var tags = new ArrayList<String>();
        tags.add("gerund");
        tags.add("past participle");
        forms.add(new Form("tested", tags));
        forms.add(new Form("testing", null));
        var glosses = new ArrayList<String>();
        glosses.add("to perform a test");
        var senses = new ArrayList<Sense>();
        senses.add(new Sense(glosses, null));
        Etymology etymology = new Etymology("test", "noun", "en", "born from nothing", "en");
        etymologyRepository.save(etymology);
        etymology.setForms(forms);
        etymology.setSenses(senses);
        forms.forEach(form -> form.setEtymology(etymology));
        senses.forEach(sense -> sense.setEtymology(etymology));
        forms.forEach(form -> formRepository.save(form));
        senses.forEach(sense -> senseRepository.save(sense));
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
            insertDataFromWiktionary();
        }
    }
}

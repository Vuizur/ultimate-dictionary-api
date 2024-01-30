package com.pux12.dictionarycreator.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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

import jakarta.annotation.PostConstruct;

@Service
public class InsertService {

    @Autowired
    private EtymologyRepository etymologyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 10000;

    public void insertDataFromWiktionary() {
        ObjectMapper mapper = new ObjectMapper();
        WiktextractDownloader downloader = new WiktextractDownloader();

        // Benchmark the time it takes to insert all the data
        long startTime = System.currentTimeMillis();

        int totalInserts = 0;

        try {
            List<Etymology> etymologies = new ArrayList<Etymology>();
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

                    if (i > 20000) {
                        break;
                    }
                    var json = mapper.readTree(line);

                    String word = null;
                    String pos = null;
                    String langCode = null;
                    String etym = null;
                    var senses = new ArrayList<Sense>();
                    var forms = new ArrayList<Form>();
                    var translations = new ArrayList<Translation>();

                    var etymology = new Etymology();

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
                            sense.setEtymology(etymology);
                            senses.add(sense);
                        }
                    }
                    if (json.has("forms")) {
                        // Iterate over the forms
                        for (var formJson : json.get("forms")) {
                            var tags = new ArrayList<String>();
                            if (formJson.has("tags")) {
                                for (var tagJson : formJson.get("tags")) {
                                    tags.add(tagJson.asText());
                                }
                            }
                            String formString = null;
                            if (formJson.has("form")) {
                                formString = formJson.get("form").asText();
                            }
                            var form = new Form(formString, tags);
                            form.setEtymology(etymology);
                            forms.add(form);
                        }
                    }
                    if (json.has("translations")) {
                        // Iterate over the translations
                        for (var translationJson : json.get("translations")) {
                            String senseString = null;
                            // TODO: integrate senseID
                            if (translationJson.has("sense")) {
                                senseString = translationJson.get("sense").asText();
                            }
                            String wordString = null;
                            if (translationJson.has("word")) {
                                wordString = translationJson.get("word").asText();
                            }
                            String langString = null;
                            if (translationJson.has("lang")) {
                                langString = translationJson.get("lang").asText();
                            }
                            String codeString = null;
                            if (translationJson.has("code")) {
                                codeString = translationJson.get("code").asText();
                            }
                            var translation = new Translation(wordString, codeString, langString, senseString);
                            translation.setEtymology(etymology);
                            translations.add(translation);
                        }
                    }
                    etymology.setWord(word);
                    etymology.setPos(pos);
                    etymology.setLangCode(langCode);
                    etymology.setEtymology(etym);
                    etymology.setSourceWiktionaryCode(sourceWiktionaryCode);
                    etymology.setSenses(senses);
                    etymology.setForms(forms);
                    etymology.setTranslations(translations);
                    etymologies.add(etymology);
                    totalInserts++;
                    i++;
                    if (totalInserts % BATCH_SIZE == 0) {
                        System.out.println("Inserting batch " + totalInserts / BATCH_SIZE);
                        etymologyRepository.saveAll(etymologies);
                        etymologies.clear();
                    }
                }
                etymologyRepository.saveAll(etymologies);

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
        System.out.println("Inserting data: " + totalInserts / ((endTime - startTime) / 1000.0) + " inserts/s");
    }

    /*
     * public void insertTestEtymology() {
     * var forms = new ArrayList<Form>();
     * var tags = new ArrayList<String>();
     * tags.add("gerund");
     * tags.add("past participle");
     * forms.add(new Form("tested", tags));
     * forms.add(new Form("testing", null));
     * var glosses = new ArrayList<String>();
     * glosses.add("to perform a test");
     * var senses = new ArrayList<Sense>();
     * senses.add(new Sense(glosses, null));
     * Etymology etymology = new Etymology("test", "noun", "en",
     * "born from nothing", "en");
     * etymologyRepository.save(etymology);
     * etymology.setForms(forms);
     * etymology.setSenses(senses);
     * forms.forEach(form -> form.setEtymology(etymology));
     * senses.forEach(sense -> sense.setEtymology(etymology));
     * forms.forEach(form -> formRepository.save(form));
     * senses.forEach(sense -> senseRepository.save(sense));
     * }
     */

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

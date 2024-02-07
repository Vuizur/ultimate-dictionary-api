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
import com.pux12.dictionarycreator.model.entity.Etymology;
import com.pux12.dictionarycreator.model.entity.Form;
import com.pux12.dictionarycreator.model.entity.Sense;
import com.pux12.dictionarycreator.model.entity.Sound;
import com.pux12.dictionarycreator.model.entity.Synonym;
import com.pux12.dictionarycreator.model.entity.Translation;
import com.pux12.dictionarycreator.repository.EtymologyRepository;

import jakarta.annotation.PostConstruct;

@Service
public class InsertService {

    @Autowired
    private EtymologyRepository etymologyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final boolean DELETE_FILES_AFTER_INSERT = true;

    private static final boolean IGNORE_FORMS = true;

    private static final int BATCH_SIZE = 10000;

    public void createIndexes() {
        String[] sqlStatements = new String[] {
                "CREATE INDEX IF NOT EXISTS etymology_word_idx ON etymology (word);",
                "CREATE INDEX IF NOT EXISTS etymology_pos_idx ON etymology (pos);",
                "CREATE INDEX IF NOT EXISTS etymology_lang_code_idx ON etymology (lang_code);",
                "CREATE INDEX IF NOT EXISTS translation_etym_lang_code_idx ON translation USING btree (etymology_id, lang_code);",
                "CREATE INDEX IF NOT EXISTS etymology_word_lang_code_idx ON etymology (word, lang_code);",
                "CREATE INDEX IF NOT EXISTS etymology_source_wiktionary_code_idx ON etymology (source_wiktionary_code);",
                "CREATE INDEX IF NOT EXISTS translation_lang_code_idx ON translation (lang_code);",
                "CREATE INDEX IF NOT EXISTS translation_word_idx ON translation (word);",
                "CREATE INDEX IF NOT EXISTS translation_etymology_id_idx ON translation (etymology_id);",
                "CREATE INDEX IF NOT EXISTs translation_etym_sense_lang_idx ON translation USING btree (etymology_id, sense, lang_code);",
                "CREATE INDEX IF NOT EXISTS sense_etymology_id_idx ON sense (etymology_id);",
                "CREATE INDEX IF NOT EXISTS sense_examples_sense_id_idx ON sense_examples (sense_id);",
                "CREATE INDEX IF NOT EXISTS sense_glosses_sense_id_idx ON sense_glosses (sense_id);",
                "CREATE INDEX IF NOT EXISTS sound_etymology_id_idx ON sound (etymology_id);",
                "CREATE INDEX IF NOT EXISTS synonym_etymology_id_idx ON synonym (etymology_id);",
                "CREATE INDEX IF NOT EXISTS form_etymology_id_idx ON form (etymology_id);",
                "CREATE INDEX IF NOT EXISTS form_form_idx ON form (form);",
                "CREATE INDEX IF NOT EXISTS form_form_tags_idx ON form_tags (form_id);"
        };
        // Execute the batch update
        jdbcTemplate.batchUpdate(sqlStatements);
    }

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
                if (!dumpFile.exists()) {
                    System.out.println("Warning: File does not exist - " + dumpFile.getAbsolutePath());
                    continue;
                }
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

                    /*
                     * if (i > 20000) {
                     * break;
                     * }
                     */
                    var json = mapper.readTree(line);

                    String word = null;
                    String pos = null;
                    String langCode = null;
                    String etym = null;
                    var senses = new ArrayList<Sense>();
                    var forms = new ArrayList<Form>();
                    var translations = new ArrayList<Translation>();
                    var synonyms = new ArrayList<Synonym>();
                    var sounds = new ArrayList<Sound>();
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
                            // If ignore_forms, only add form if tags contains "canonical" (which is important)
                            if (IGNORE_FORMS && !tags.contains("canonical")) {
                                continue;
                            } else {
                                var form = new Form(formString, tags);
                                form.setEtymology(etymology);
                                forms.add(form);
                            }
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
                            } else if (translationJson.has("lang_code")) {
                                codeString = translationJson.get("lang_code").asText();
                            }
                            List<String> tags = new ArrayList<String>();
                            if (translationJson.has("tags")) {
                                for (var tag : translationJson.get("tags")) {
                                    tags.add(tag.asText());
                                }
                            }
                            List<String> senseIds = new ArrayList<String>();
                            if (translationJson.has("senseids")) {
                                for (var senseId : translationJson.get("senseids")) {
                                    senseIds.add(senseId.asText());
                                }
                            }
                            var translation = new Translation(wordString, codeString, langString, senseString, tags,
                                    senseIds);
                            translation.setEtymology(etymology);
                            translations.add(translation);
                        }
                    }
                    if (json.has("synonyms")) {
                        for (var synonymJson : json.get("synonyms")) {
                            String wordString = null;
                            if (synonymJson.has("word")) {
                                wordString = synonymJson.get("word").asText();
                            }
                            var synonym = new Synonym(wordString);
                            synonym.setEtymology(etymology);
                            synonyms.add(synonym);

                        }
                    }
                    if (json.has("sounds")) {
                        for (var soundJson : json.get("sounds")) {
                            String ipa = null;
                            if (soundJson.has("ipa")) {
                                ipa = soundJson.get("ipa").asText();
                            } else {
                                continue;
                            }
                            var sound = new Sound(ipa);
                            sound.setEtymology(etymology);
                            sounds.add(sound);
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
                    etymology.setSynonyms(synonyms);
                    etymology.setSounds(sounds);

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
                if (DELETE_FILES_AFTER_INSERT) {
                    dumpFile.delete();
                }
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

    @PostConstruct
    public void insertData() {

        // createIndexes(); // TODO: REMOVE!

        System.out.println("Inserting data");
        var res = jdbcTemplate.query(
                "select true from etymology limit 1;", (resultSet, rowNum) -> resultSet.getBoolean(1));
        if (!res.isEmpty()) {
            System.out.println("Data already inserted");
        } else {
            System.out.println("Inserting data");
            insertDataFromWiktionary();
            System.out.println("Creating indexes");
            long startTime = System.currentTimeMillis();
            createIndexes();
            long endTime = System.currentTimeMillis();
            // Print with HH:MM:SS.MS format
            System.out.println("Creating indexes took " + String.format("%02d:%02d:%02d.%04d",
                    (endTime - startTime) / 3600000, ((endTime - startTime) / 60000) % 60,
                    ((endTime - startTime) / 1000) % 60, (endTime - startTime) % 1000));
        }
    }
}

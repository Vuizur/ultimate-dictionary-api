package com.pux12.dictionarycreator.datainsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
// jackson json

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pux12.dictionarycreator.repository.EtymologyRepository;

public class InsertData {
    public static final String url = "jdbc:postgresql://localhost:5432/wikt";
    public static final String user = "postgres";
    public static final String password = "silver";

    public static void main(String[] args) throws java.io.IOException {

        try {
            Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println(WiktextractDownloader.getWiktionaryDumpPaths()[3]);
            ObjectMapper mapper = new ObjectMapper();

            // For each dump, print the first 10 lines
            for (var dumpPath : WiktextractDownloader.getWiktionaryDumpPaths()) {

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
                for (int i = 0; i < 100; i++) {
                    // Parse the line as JSON
                    var line = dumpReader.readLine();
                    var json = mapper.readTree(line);

                    String word = null;
                    String pos = null;
                    String langCode = null;
                    String senses = null;
                    String translations = null;

                    // Get the word if it exists
                    if (json.has("word")) {
                        word = json.get("word").asText();
                    }
                    if (json.has("pos")) {
                        pos = json.get("pos").asText();
                    }
                    if (json.has("lang_code")) {
                        langCode = json.get("lang_code").asText();
                    }
                    if (json.has("senses")) {
                        senses = json.get("senses").asText();
                    }
                    if (json.has("translations")) {
                        translations = json.get("translations").asText();
                    }

                    // Insert the word into the database
                    //var insertWord = conn.prepareStatement(
                    //        "INSERT INTO etym (word, pos, lang_code, senses, translations, source_wiktionary_code) VALUES (?, ?, ?, ?, ?, ?)");
                    //insertWord.setString(1, word);
                    //insertWord.setString(2, pos);
                    //insertWord.setString(3, langCode);
                    //insertWord.setString(4, senses);
                    //insertWord.setString(5, translations);
                    //insertWord.setString(6, sourceWiktionaryCode);
                    //insertWord.executeUpdate();
                    //insertWord.close();

                }


                dumpReader.close();
            }

            conn.close();
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

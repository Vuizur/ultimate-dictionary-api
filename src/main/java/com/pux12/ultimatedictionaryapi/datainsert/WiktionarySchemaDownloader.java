package com.pux12.ultimatedictionaryapi.datainsert;

// Ignore this
// I currently don't use it

public class WiktionarySchemaDownloader {

    public static final String[] languages = { "de", "es", "fr", "ru", "zh" };
    public static final String schemaBaseDir = "src/main/resources/wiktionary/";

    public static String[] getSchemaFilePaths() {
        String[] schemaFilePaths = new String[languages.length];
        for (int i = 0; i < languages.length; i++) {
            schemaFilePaths[i] = schemaBaseDir + languages[i] + ".json";
        }
        return schemaFilePaths;
    }

    public static void main(String[] args) {
        // Schemas are in form of https://tatuylonen.github.io/wiktextract/de.json
        // Download for languages de, es, fr, ru, zh
        for (var language : languages) {
            var url = "https://tatuylonen.github.io/wiktextract/" + language + ".json";
            var filename = schemaBaseDir + language + ".json";
            System.out.println("Downloading " + url + " to " + filename);
            try {
                var uri = new java.net.URI(url);
                var connection = uri.toURL().openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                var inputStream = connection.getInputStream();

                var file = new java.io.File(filename);
                var parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                var outputStream = new java.io.FileOutputStream(filename);
                var buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                System.out.println("Error downloading " + url + ": " + e);
            }
        }

    }

}

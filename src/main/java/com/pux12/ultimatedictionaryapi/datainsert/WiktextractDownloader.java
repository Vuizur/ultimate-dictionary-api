package com.pux12.ultimatedictionaryapi.datainsert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

public class WiktextractDownloader {

    public String outDir;

    public static String[] urls = {
            "https://kaikki.org/dictionary/raw-wiktextract-data.json.gz",
            "https://kaikki.org/dictionary/downloads/zh/zh-extract.json.gz",
            "https://kaikki.org/dictionary/downloads/fr/fr-extract.json.gz",
            "https://kaikki.org/dictionary/downloads/de/de-extract.json.gz",
            "https://kaikki.org/dictionary/downloads/ru/ru-extract.json.gz",
            "https://kaikki.org/dictionary/downloads/es/es-extract.json.gz"
    };

    public String[] getWiktionaryDumpPaths() {
        String[] schemaFilePaths = new String[urls.length];
        for (int i = 0; i < urls.length; i++) {
            schemaFilePaths[i] = outDir + urls[i].substring(urls[i].lastIndexOf('/') + 1, urls[i].lastIndexOf('.'));
        }
        return schemaFilePaths;
    }

    // Define a method to download and extract a file from a URL
    public static void downloadAndExtract(String url, String outputDir) throws IOException {
        // Create a URL object from the string
        URL fileURL = new URL(url);
        // Get the file name from the URL
        String fileName = fileURL.getFile().substring(fileURL.getFile().lastIndexOf('/') + 1);
        // Create a file output stream to save the downloaded file
        FileOutputStream fos = new FileOutputStream(outputDir + fileName);
        // Create a buffered input stream to read from the URL connection
        BufferedInputStream bis = new BufferedInputStream(fileURL.openConnection().getInputStream());
        // Create a buffer to store the data
        byte[] buffer = new byte[1024];
        // Read from the input stream and write to the output stream
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fos.write(buffer, 0, count);
        }
        // Close the streams
        fos.close();
        bis.close();
        // Create a gzip input stream to read the compressed file
        GZIPInputStream gis = new GZIPInputStream(new FileInputStream(outputDir + fileName));
        // Create a file output stream to write the extracted file
        fos = new FileOutputStream(outputDir + fileName.substring(0, fileName.lastIndexOf('.')));
        // Read from the gzip input stream and write to the output stream
        while ((count = gis.read(buffer, 0, 1024)) != -1) {
            fos.write(buffer, 0, count);
        }
        // Close the streams
        fos.close();
        gis.close();
        // Delete the compressed file
        new File(outputDir + fileName).delete();
    }

    public void downloadAllWiktionaries() throws IOException {
        // Define the URLs to download and extract
        String[] urls = {
                "https://kaikki.org/dictionary/raw-wiktextract-data.json.gz",
                "https://kaikki.org/dictionary/downloads/zh/zh-extract.json.gz",
                "https://kaikki.org/dictionary/downloads/fr/fr-extract.json.gz",
                "https://kaikki.org/dictionary/downloads/de/de-extract.json.gz",
                "https://kaikki.org/dictionary/downloads/ru/ru-extract.json.gz",
                "https://kaikki.org/dictionary/downloads/es/es-extract.json.gz"
        };
        // Loop through the URLs and call the download and extract method
        for (String url : urls) {
            downloadAndExtract(url, outDir);
        }
    }

    public WiktextractDownloader() {
        var prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/custom.properties"));
            outDir = prop.getProperty("outDir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define a main method to test the download and extract method
    public static void main(String[] args) throws IOException {

        var downloader = new WiktextractDownloader();
        System.out.println(downloader.outDir);
        downloader.downloadAllWiktionaries();
    }
}

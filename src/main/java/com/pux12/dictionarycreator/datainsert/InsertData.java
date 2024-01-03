package com.pux12.dictionarycreator.datainsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

public class InsertData {

    public static void main(String[] args) throws java.io.IOException {
        String url = "jdbc:postgresql://localhost:5432/wikt";
        String user = "postgres";
        String password = "silver";
        try {
            Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println(WiktextractDownloader.getWiktionaryDumpPaths()[3]);

            System.out.println("Это тест");
            System.exit(0);

            // For each dump, print the first 10 lines
            for (var dumpPath : WiktextractDownloader.getWiktionaryDumpPaths()) {
                var dumpFile = new File(dumpPath);
                var dumpReader = new BufferedReader(new FileReader(dumpFile));
                // Print 
                // #########
                // <name of dump>
                System.out.println("#########");
                System.out.println(dumpFile.getName());
                for (int i = 0; i < 3; i++) {
                    System.out.println(dumpReader.readLine());
                }
                dumpReader.close();
            }

            conn.close();
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

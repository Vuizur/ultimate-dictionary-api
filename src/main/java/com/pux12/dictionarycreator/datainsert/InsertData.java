package com.pux12.dictionarycreator.datainsert;
import java.sql.Connection;

public class InsertData {

    

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/wikt";
        String user = "postgres";
        String password = "silver";
        try {
            Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            


            conn.close();
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

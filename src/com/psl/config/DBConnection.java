package com.psl.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=PSL_Auction;encrypt=true;trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASSWORD = "StrongPass@123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MSSQL successfully!");
            return conn;

        } catch (Exception e) {
            System.out.println("Connection failed! error: {" + e.getMessage() + "}");
            return null;
        }
    }
}
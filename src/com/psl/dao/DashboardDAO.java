package com.psl.dao;

import com.psl.config.DBConnection;

import java.sql.*;

public class DashboardDAO {

    private String[] fetch(String sql) {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);

            if (rs.next()) {

                String name = rs.getString(1);

                double val = rs.getDouble(2);

                // ✅ FIX: round to 2 decimal
                String formatted = String.format("%.2f", val);

                return new String[]{name, formatted};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{"N/A", "0"};
    }

    // OLD
    public String[] topInternational() {
        return fetch("""
                    SELECT TOP 1 p.Player_Name, a.Final_Price
                    FROM Auction a
                    JOIN Player p ON a.Player_ID=p.Player_ID
                    WHERE p.Nationality!='Pakistan'
                    AND a.IsDeleted=0
                    ORDER BY a.Final_Price DESC
                """);
    }

    public String[] topNational() {
        return fetch("""
                    SELECT TOP 1 p.Player_Name, a.Final_Price
                    FROM Auction a
                    JOIN Player p ON a.Player_ID=p.Player_ID
                    WHERE p.Nationality='Pakistan'
                    AND a.IsDeleted=0
                    ORDER BY a.Final_Price DESC
                """);
    }

    public String[] highestPaid() {
        return fetch("""
                    SELECT TOP 1 p.Player_Name, a.Final_Price
                    FROM Auction a
                    JOIN Player p ON a.Player_ID=p.Player_ID
                    WHERE a.IsDeleted=0
                    ORDER BY a.Final_Price DESC
                """);
    }

    public String[] topSpendingTeam() {
        return fetch("""
                    SELECT TOP 1 t.Team_Name, SUM(a.Final_Price)
                    FROM Auction a
                    JOIN Team t ON a.Team_ID=t.Team_ID
                    WHERE a.IsDeleted=0
                    GROUP BY t.Team_Name
                    ORDER BY SUM(a.Final_Price) DESC
                """);
    }

    public String[] maxPlayersTeam() {
        return fetch("""
                    SELECT TOP 1 t.Team_Name, COUNT(*)
                    FROM Auction a
                    JOIN Team t ON a.Team_ID=t.Team_ID
                    WHERE a.IsDeleted=0
                    GROUP BY t.Team_Name
                    ORDER BY COUNT(*) DESC
                """);
    }

}
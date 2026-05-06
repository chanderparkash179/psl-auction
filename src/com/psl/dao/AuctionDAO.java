package com.psl.dao;

import com.psl.config.DBConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class AuctionDAO {

    // ================= UPDATE AUCTION =================
    public void updateAuction(int auctionId, int playerId, int teamId, int categoryId,
                              double price, String round, String type, String status) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                    UPDATE Auction SET
                    Player_ID=?,
                    Team_ID=?,
                    Category_ID=?,
                    Final_Price=?,
                    Auction_Round=?,
                    Acquisition_Type=?,
                    Status=?
                    WHERE Auction_ID=? AND IsDeleted=0
                    """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, playerId);
            ps.setInt(2, teamId);
            ps.setInt(3, categoryId);
            ps.setDouble(4, price);
            ps.setString(5, round);
            ps.setString(6, type);
            ps.setString(7, status);
            ps.setInt(8, auctionId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTopInternational() {
        String sql = "SELECT TOP 1 p.Player_Name FROM Auction a " +
                "JOIN Player p ON a.Player_ID = p.Player_ID " +
                "WHERE p.Nationality != 'Pakistan' AND a.IsDeleted = 0 " +
                "ORDER BY a.Final_Price DESC";

        return fetchSingle(sql);
    }

    public String getTopNational() {
        String sql = "SELECT TOP 1 p.Player_Name FROM Auction a " +
                "JOIN Player p ON a.Player_ID = p.Player_ID " +
                "WHERE p.Nationality = 'Pakistan' AND a.IsDeleted = 0 " +
                "ORDER BY a.Final_Price DESC";

        return fetchSingle(sql);
    }

    public String getHighestPaid() {
        String sql = "SELECT TOP 1 p.Player_Name FROM Auction a " +
                "JOIN Player p ON a.Player_ID = p.Player_ID " +
                "WHERE a.IsDeleted = 0 " +
                "ORDER BY a.Final_Price DESC";

        return fetchSingle(sql);
    }

    public String getTopSpendingTeam() {
        String sql = "SELECT TOP 1 t.Team_Name FROM Auction a " +
                "JOIN Team t ON a.Team_ID = t.Team_ID " +
                "WHERE a.IsDeleted = 0 " +
                "GROUP BY t.Team_Name " +
                "ORDER BY SUM(a.Final_Price) DESC";

        return fetchSingle(sql);
    }

    public DefaultTableModel getAuctionTable(String search, String filter) {

        String sql = "SELECT a.Auction_ID, p.Player_Name, t.Team_Name, " +
                "a.Final_Price, a.Status, a.Auction_Round " +
                "FROM Auction a " +
                "JOIN Player p ON a.Player_ID = p.Player_ID " +
                "JOIN Team t ON a.Team_ID = t.Team_ID " +
                "WHERE a.IsDeleted = 0 ";

        if (search != null && !search.isEmpty()) {
            sql += " AND (p.Player_Name LIKE '%" + search + "%' OR t.Team_Name LIKE '%" + search + "%')";
        }

        if (filter != null && !filter.equals("All")) {
            sql += " AND a.Status = '" + filter + "'";
        }

        sql += " ORDER BY a.Auction_ID DESC";

        return buildTable(sql);
    }

    public void softDelete(int auctionId) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Auction SET IsDeleted = 1 WHERE Auction_ID = ?"
            );
            ps.setInt(1, auctionId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchSingle(String sql) {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            if (rs.next()) return rs.getString(1);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return "N/A";
    }

    private DefaultTableModel buildTable(String sql) {
        DefaultTableModel model = new DefaultTableModel();

        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);

            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            for (int i = 1; i <= cols; i++)
                model.addColumn(md.getColumnName(i));

            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 1; i <= cols; i++)
                    row[i - 1] = rs.getObject(i);
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}

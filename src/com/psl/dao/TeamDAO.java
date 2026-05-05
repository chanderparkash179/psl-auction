package com.psl.dao;

import com.psl.config.DBConnection;
import com.psl.model.Team;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class TeamDAO {

    // ================= GET TEAMS =================
    public DefaultTableModel getTeams(String search) {

        String sql = "SELECT Team_ID, Team_Name FROM Team WHERE IsDeleted = 0";

        if (search != null && !search.isEmpty()) {
            sql += " AND Team_Name LIKE '%" + search + "%'";
        }

        return buildTable(sql);
    }

    // ================= GET BY ID =================
    public Team getById(int id) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM Team WHERE Team_ID = ?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Team t = new Team();
                t.setTeamId(id);
                t.setTeamName(rs.getString("Team_Name"));
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= SAVE =================
    public void save(Team t) {
        try {
            Connection con = DBConnection.getConnection();

            if (t.getTeamId() == 0) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Team VALUES (?, 0)"
                );
                ps.setString(1, t.getTeamName());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Team SET Team_Name=? WHERE Team_ID=?"
                );
                ps.setString(1, t.getTeamName());
                ps.setInt(2, t.getTeamId());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SOFT DELETE =================
    public void softDelete(int id) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Team SET IsDeleted = 1 WHERE Team_ID = ?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= TEAM SQUAD =================
    public String getTeamPlayers(String teamName) {

        String sql =
                "SELECT p.Player_Name FROM Auction a " +
                        "JOIN Player p ON a.Player_ID = p.Player_ID " +
                        "JOIN Team t ON a.Team_ID = t.Team_ID " +
                        "WHERE t.Team_Name = '" + teamName + "' AND a.IsDeleted = 0";

        StringBuilder sb = new StringBuilder();

        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                sb.append(rs.getString(1)).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.length() == 0 ? "No Players" : sb.toString();
    }

    // ================= TABLE BUILDER =================
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
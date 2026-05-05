package com.psl.dao;

import com.psl.config.DBConnection;
import com.psl.model.Player;
import com.psl.model.PlayerCareer;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PlayerDAO {

    public Player getById(int id) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM Player WHERE Player_ID = ?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Player p = new Player();
                p.setPlayerId(id);
                p.setPlayerName(rs.getString("Player_Name"));
                p.setNationality(rs.getString("Nationality"));
                p.setPrimaryRole(rs.getString("Primary_Role"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void softDelete(int id) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Player SET IsDeleted = 1 WHERE Player_ID = ?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Player p) {
        try {
            Connection con = DBConnection.getConnection();

            if (p.getPlayerId() == 0) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Player VALUES (?, ?, ?, 0)"
                );
                ps.setString(1, p.getPlayerName());
                ps.setString(2, p.getNationality());
                ps.setString(3, p.getPrimaryRole());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Player SET Player_Name=?, Nationality=?, Primary_Role=? WHERE Player_ID=?"
                );
                ps.setString(1, p.getPlayerName());
                ps.setString(2, p.getNationality());
                ps.setString(3, p.getPrimaryRole());
                ps.setInt(4, p.getPlayerId());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public PlayerCareer getByPlayerId(int playerId) {
        PlayerCareer pc = null;

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM PlayerCareer WHERE Player_ID = ? AND IsDeleted = 0"
            );
            ps.setInt(1, playerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pc = new PlayerCareer();
                pc.setPlayerId(playerId);
                pc.setPslMatches(rs.getInt("PSL_Matches"));
                pc.setTotalRuns(rs.getInt("Total_Runs"));
                pc.setBattingAvg(rs.getDouble("Batting_Avg"));
                pc.setStrikeRate(rs.getDouble("Strike_Rate"));
                pc.setWickets(rs.getInt("Wickets"));
                pc.setEconomy(rs.getDouble("Economy"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pc;
    }

    public String getCurrentTeam(int playerId) {

        String team = "-";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT TOP 1 t.Team_Name " +
                            "FROM Auction a " +
                            "JOIN Team t ON a.Team_ID = t.Team_ID " +
                            "WHERE a.Player_ID = ? AND a.IsDeleted = 0 " +
                            "ORDER BY a.Auction_ID DESC"
            );

            ps.setInt(1, playerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                team = rs.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return team;
    }

    public void update(PlayerCareer pc) {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE PlayerCareer SET " +
                            "PSL_Matches=?, Total_Runs=?, Batting_Avg=?, Strike_Rate=?, Wickets=?, Economy=? " +
                            "WHERE Player_ID=?"
            );

            ps.setInt(1, pc.getPslMatches());
            ps.setInt(2, pc.getTotalRuns());
            ps.setDouble(3, pc.getBattingAvg());
            ps.setDouble(4, pc.getStrikeRate());
            ps.setInt(5, pc.getWickets());
            ps.setDouble(6, pc.getEconomy());
            ps.setInt(7, pc.getPlayerId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public DefaultTableModel getPlayers(String search, String team) {

        String[] cols = {"ID", "Name", "Nationality", "Role"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                        SELECT Player_ID, Player_Name, Nationality, Primary_Role
                        FROM Player
                        WHERE IsDeleted=0
                    """;

            if (search != null && !search.isEmpty()) {
                sql += " AND Player_Name LIKE ?";
            }

            PreparedStatement ps = con.prepareStatement(sql);

            if (search != null && !search.isEmpty()) {
                ps.setString(1, "%" + search + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
package com.psl.dao;

import com.psl.config.DBConnection;
import com.psl.model.PlayerCareer;

import java.sql.*;

public class PlayerCareerDAO {

    public PlayerCareer getByPlayerId(int playerId) {

        PlayerCareer pc = null;

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM PlayerCareer WHERE Player_ID=? AND IsDeleted=0"
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
                    """
                            SELECT TOP 1 t.Team_Name
                            FROM Auction a
                            JOIN Team t ON a.Team_ID = t.Team_ID
                            WHERE a.Player_ID = ?
                            AND a.IsDeleted = 0
                            ORDER BY a.Auction_ID DESC
                            """
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
                    """
                            UPDATE PlayerCareer SET
                            PSL_Matches=?,
                            Total_Runs=?,
                            Batting_Avg=?,
                            Strike_Rate=?,
                            Wickets=?,
                            Economy=?
                            WHERE Player_ID=?
                            """
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
}
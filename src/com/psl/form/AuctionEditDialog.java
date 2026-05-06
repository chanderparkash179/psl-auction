package com.psl.form;

import com.psl.config.DBConnection;
import com.psl.dao.AuctionDAO;
import com.psl.view.DashboardFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AuctionEditDialog extends JDialog {

    private JComboBox<String> playerBox, teamBox, categoryBox, statusBox, roundBox;
    private JTextField priceField;

    private int auctionId;
    private DashboardFrame parent;

    public AuctionEditDialog(JFrame parentFrame, int auctionId, DashboardFrame parent) {
        super(parentFrame, "Edit Auction", true);

        this.auctionId = auctionId;
        this.parent = parent;

        setSize(450, 350);
        setLocationRelativeTo(parentFrame);
        setLayout(new GridLayout(7, 2, 10, 10));

        playerBox = new JComboBox<>();
        teamBox = new JComboBox<>();
        categoryBox = new JComboBox<>();
        statusBox = new JComboBox<>(new String[]{"Sold", "Retained", "Unsold"});
        roundBox = new JComboBox<>(new String[]{"Pre-Auction", "Round 1", "Round 2"});
        priceField = new JTextField();

        add(new JLabel("Player"));
        add(playerBox);
        add(new JLabel("Team"));
        add(teamBox);
        add(new JLabel("Category"));
        add(categoryBox);
        add(new JLabel("Status"));
        add(statusBox);
        add(new JLabel("Round"));
        add(roundBox);
        add(new JLabel("Price"));
        add(priceField);

        JButton save = new JButton("Update");
        add(save);

        loadData();
        loadAuction();

        save.addActionListener(e -> update());
    }

    private void loadData() {
        try {
            Connection con = DBConnection.getConnection();

            ResultSet p = con.createStatement().executeQuery("SELECT Player_Name FROM Player WHERE IsDeleted=0");
            while (p.next()) playerBox.addItem(p.getString(1));

            ResultSet t = con.createStatement().executeQuery("SELECT Team_Name FROM Team WHERE IsDeleted=0");
            while (t.next()) teamBox.addItem(t.getString(1));

            ResultSet c = con.createStatement().executeQuery("SELECT Category_Name FROM Category WHERE IsDeleted=0");
            while (c.next()) categoryBox.addItem(c.getString(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAuction() {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    """
                            SELECT p.Player_Name, t.Team_Name, c.Category_Name,
                                   a.Final_Price, a.Status, a.Auction_Round
                            FROM Auction a
                            JOIN Player p ON a.Player_ID=p.Player_ID
                            JOIN Team t ON a.Team_ID=t.Team_ID
                            JOIN Category c ON a.Category_ID=c.Category_ID
                            WHERE a.Auction_ID=?
                            """
            );

            ps.setInt(1, auctionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                playerBox.setSelectedItem(rs.getString(1));
                teamBox.setSelectedItem(rs.getString(2));
                categoryBox.setSelectedItem(rs.getString(3));
                priceField.setText(rs.getString(4));
                statusBox.setSelectedItem(rs.getString(5));
                roundBox.setSelectedItem(rs.getString(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {

        try {
            int playerId = getId("Player", playerBox.getSelectedItem().toString());
            int teamId = getId("Team", teamBox.getSelectedItem().toString());
            int categoryId = getId("Category", categoryBox.getSelectedItem().toString());

            new AuctionDAO().updateAuction(
                    auctionId,
                    playerId,
                    teamId,
                    categoryId,
                    Double.parseDouble(priceField.getText()),
                    roundBox.getSelectedItem().toString(),
                    "Manual",
                    statusBox.getSelectedItem().toString()
            );

            parent.dispose();
            new DashboardFrame().setVisible(true);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating!");
        }
    }

    private int getId(String table, String name) throws Exception {
        Connection con = DBConnection.getConnection();
        ResultSet rs = con.createStatement()
                .executeQuery("SELECT " + table + "_ID FROM " + table + " WHERE " + table + "_Name='" + name + "'");
        return rs.next() ? rs.getInt(1) : -1;
    }
}
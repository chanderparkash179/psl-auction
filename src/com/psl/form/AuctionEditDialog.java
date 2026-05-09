package com.psl.form;

import com.psl.config.DBConnection;
import com.psl.dao.AuctionDAO;
import com.psl.view.DashboardFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuctionEditDialog extends JDialog {

    private final JComboBox<String> playerBox;
    private final JComboBox<String> teamBox;
    private final JComboBox<String> categoryBox;
    private final JComboBox<String> statusBox;
    private final JComboBox<String> roundBox;

    private final JTextField priceField;

    private final int auctionId;

    private final DashboardFrame parent;

    public AuctionEditDialog(
            JFrame parentFrame,
            int auctionId,
            DashboardFrame parent
    ) {

        super(parentFrame, "Edit Auction", true);

        this.auctionId = auctionId;
        this.parent = parent;

        setSize(500, 400);
        setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(
                new GridLayout(7, 2, 10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        playerBox = new JComboBox<>();

        teamBox = new JComboBox<>();

        categoryBox = new JComboBox<>();

        statusBox = new JComboBox<>(
                new String[]{
                        "Sold",
                        "Retained",
                        "Unsold"
                }
        );

        roundBox = new JComboBox<>(
                new String[]{
                        "Pre-Auction",
                        "Round 1",
                        "Round 2"
                }
        );

        priceField = new JTextField();

        panel.add(new JLabel("Player"));
        panel.add(playerBox);

        panel.add(new JLabel("Team"));
        panel.add(teamBox);

        panel.add(new JLabel("Category"));
        panel.add(categoryBox);

        panel.add(new JLabel("Status"));
        panel.add(statusBox);

        panel.add(new JLabel("Round"));
        panel.add(roundBox);

        panel.add(new JLabel("Price"));
        panel.add(priceField);

        JButton btnUpdate = new JButton("Update Auction");

        panel.add(btnUpdate);

        add(panel);

        loadDropdowns();
        loadAuctionData();

        btnUpdate.addActionListener(e -> updateAuction());
    }

    private void loadDropdowns() {

        try {

            Connection con = DBConnection.getConnection();

            ResultSet p = con.createStatement().executeQuery(
                    "SELECT Player_Name FROM Player WHERE IsDeleted=0"
            );

            while (p.next()) {
                playerBox.addItem(p.getString(1));
            }

            ResultSet t = con.createStatement().executeQuery(
                    "SELECT Team_Name FROM Team WHERE IsDeleted=0"
            );

            while (t.next()) {
                teamBox.addItem(t.getString(1));
            }

            ResultSet c = con.createStatement().executeQuery(
                    "SELECT Category_Name FROM Category WHERE IsDeleted=0"
            );

            while (c.next()) {
                categoryBox.addItem(c.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAuctionData() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT p.Player_Name, " +
                            "t.Team_Name, " +
                            "c.Category_Name, " +
                            "a.Final_Price, " +
                            "a.Status, " +
                            "a.Auction_Round " +
                            "FROM Auction a " +
                            "JOIN Player p ON a.Player_ID = p.Player_ID " +
                            "JOIN Team t ON a.Team_ID = t.Team_ID " +
                            "JOIN Category c ON a.Category_ID = c.Category_ID " +
                            "WHERE a.Auction_ID=?"
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

    private void updateAuction() {

        try {

            int playerId = getId(
                    "Player",
                    "Player_Name",
                    playerBox.getSelectedItem().toString()
            );

            int teamId = getId(
                    "Team",
                    "Team_Name",
                    teamBox.getSelectedItem().toString()
            );

            int categoryId = getId(
                    "Category",
                    "Category_Name",
                    categoryBox.getSelectedItem().toString()
            );

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

            JOptionPane.showMessageDialog(
                    this,
                    "Auction Updated Successfully!"
            );

            dispose();

            parent.dispose();

            new DashboardFrame().setVisible(true);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error Updating Auction!"
            );
        }
    }

    private int getId(
            String table,
            String column,
            String value
    ) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT " + table + "_ID FROM " +
                        table +
                        " WHERE " + column + "=?"
        );

        ps.setString(1, value);

        ResultSet rs = ps.executeQuery();

        return rs.next() ? rs.getInt(1) : -1;
    }
}
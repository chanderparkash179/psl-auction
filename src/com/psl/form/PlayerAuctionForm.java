package com.psl.form;

import com.psl.config.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlayerAuctionForm extends JFrame {

    JComboBox<String> teamBox, typeBox, statusBox, roundBox;
    JTextField priceField;

    int playerId;

    public PlayerAuctionForm(int playerId, String name) {

        this.playerId = playerId;

        setTitle("Auction - " + name);
        setSize(450, 350);
        setLayout(null);

        teamBox = new JComboBox<>();
        typeBox = new JComboBox<>(new String[]{"Retention", "Sold", "Direct Signing", "Auction"});
        statusBox = new JComboBox<>(new String[]{"Signed", "Retained", "Unsold"});
        roundBox = new JComboBox<>(new String[]{"Pre-Auction", "Round 1", "Round 2"});
        priceField = new JTextField();

        teamBox.setBounds(150, 30, 200, 25);
        typeBox.setBounds(150, 70, 200, 25);
        statusBox.setBounds(150, 110, 200, 25);
        priceField.setBounds(150, 150, 200, 25);
        roundBox.setBounds(150, 190, 200, 25);

        add(teamBox);
        add(typeBox);
        add(statusBox);
        add(priceField);
        add(roundBox);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(150, 240, 120, 30);
        add(btnSave);

        loadTeams();

        btnSave.addActionListener(e -> save());
    }

    private void loadTeams() {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT Team_Name FROM Team WHERE IsDeleted=0");

            while (rs.next()) teamBox.addItem(rs.getString(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Auction VALUES (?,?,?,?,?,?,?,0)"
            );

            ps.setInt(1, playerId);
            ps.setInt(2, getId(teamBox.getSelectedItem().toString()));
            ps.setInt(3, 1);
            ps.setDouble(4, Double.parseDouble(priceField.getText()));
            ps.setString(5, roundBox.getSelectedItem().toString());
            ps.setString(6, typeBox.getSelectedItem().toString());
            ps.setString(7, statusBox.getSelectedItem().toString());

            ps.executeUpdate();

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getId(String name) throws Exception {
        Connection con = DBConnection.getConnection();
        ResultSet rs = con.createStatement()
                .executeQuery("SELECT Team_ID FROM Team WHERE Team_Name='" + name + "'");
        return rs.next() ? rs.getInt(1) : -1;
    }
}